package com.example.hotel_nebula.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.hotel_nebula.data.ApiClient
import com.example.hotel_nebula.data.AvaliacoesResumo
import com.example.hotel_nebula.data.DashboardFaturamento
import com.example.hotel_nebula.data.Hospede
import com.example.hotel_nebula.data.HotelRepository
import com.example.hotel_nebula.data.Quarto
import com.example.hotel_nebula.data.Reserva
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime

data class GuestFormState(
    val idHospede: String = "",
    val nome: String = "",
    val email: String = "",
    val cpf: String = "",
    val telefone: String = "",
    val dataNascimento: String = "",
    val ativo: Boolean = true
)

data class ReservaFormState(
    val idReserva: String = "",
    val idHospede: String = "",
    val idQuarto: String = "",
    val dataCheckin: String = "",
    val dataCheckout: String = "",
    val status: String = "confirmada",
    val canal: String = "site_proprio",
    val valorPrevisto: String = ""
)

data class AppUiState(
    val baseUrl: String = ApiClient.DEFAULT_BASE_URL,
    val loading: Boolean = false,
    val message: String = "",
    val isError: Boolean = false,
    val hospedes: List<Hospede> = emptyList(),
    val hospedeEncontrado: Hospede? = null,
    val guestForm: GuestFormState = GuestFormState(),
    val quartos: List<Quarto> = emptyList(),
    val reservaForm: ReservaFormState = ReservaFormState(),
    val faturamento: DashboardFaturamento? = null,
    val avaliacoes: AvaliacoesResumo? = null
)

class HotelViewModel(application: Application) : AndroidViewModel(application) {
    private val preferences = application.getSharedPreferences("hotel-nebula", Context.MODE_PRIVATE)
    private val savedBaseUrl = preferences.getString("base-url", ApiClient.DEFAULT_BASE_URL) ?: ApiClient.DEFAULT_BASE_URL
    private var repository = HotelRepository(savedBaseUrl)

    private val _uiState = MutableStateFlow(AppUiState(baseUrl = savedBaseUrl))
    val uiState: StateFlow<AppUiState> = _uiState.asStateFlow()

    init {
        // Load the same first-screen data the web UI fetches when each page opens.
        carregarHospedes()
        carregarQuartos()
        carregarDashboard()
    }

    fun salvarBaseUrl(url: String) {
        val normalizedUrl = if (url.endsWith("/")) url else "$url/"
        preferences.edit().putString("base-url", normalizedUrl).apply()
        repository = HotelRepository(normalizedUrl)
        _uiState.value = _uiState.value.copy(
            baseUrl = normalizedUrl,
            message = "Configuracao salva. As proximas chamadas usarao esta API.",
            isError = false
        )
    }

    fun atualizarGuestForm(update: GuestFormState.() -> GuestFormState) {
        _uiState.value = _uiState.value.copy(guestForm = _uiState.value.guestForm.update())
    }

    fun atualizarReservaForm(update: ReservaFormState.() -> ReservaFormState) {
        _uiState.value = _uiState.value.copy(reservaForm = _uiState.value.reservaForm.update())
    }

    fun carregarHospedes() = launch("Lista carregada com sucesso.") {
        repository.listarHospedes().onSuccess { hospedes ->
            _uiState.value = _uiState.value.copy(hospedes = hospedes)
        }
    }

    fun buscarHospedePorId(idText: String) {
        val id = idText.toIntOrNull()
        if (id == null || id <= 0) {
            showMessage("Informe um ID de hospede valido.", true)
            return
        }

        launch(successMessage = null) {
            repository.buscarHospede(id).onSuccess { hospede ->
                _uiState.value = _uiState.value.copy(
                    hospedeEncontrado = hospede,
                    message = "Encontrado: ${hospede.nome} (${hospede.email})",
                    isError = false
                )
            }
        }
    }

    fun criarHospede() {
        val hospede = buildHospedeFromForm() ?: return
        launch("Hospede criado com sucesso.") {
            repository.criarHospede(hospede)
        }.invokeOnCompletion { carregarHospedes() }
    }

    fun atualizarHospede() {
        val hospede = buildHospedeFromForm() ?: return
        launch("Hospede atualizado com sucesso.") {
            repository.atualizarHospede(hospede.idHospede, hospede)
        }.invokeOnCompletion { carregarHospedes() }
    }

    fun excluirHospede(id: Int) {
        launch("Hospede removido com sucesso.") {
            repository.excluirHospede(id)
        }.invokeOnCompletion { carregarHospedes() }
    }

    fun carregarQuartos() = launch("Quartos disponiveis carregados.") {
        repository.listarQuartosDisponiveis().onSuccess { quartos ->
            _uiState.value = _uiState.value.copy(quartos = quartos)
        }
    }

    fun criarReserva() {
        val reserva = buildReservaFromForm() ?: return
        launch("Reserva criada com sucesso.") {
            repository.criarReserva(reserva)
        }
    }

    fun carregarDashboard() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(loading = true)
            val faturamento = repository.buscarFaturamento()
            val avaliacoes = repository.buscarResumoAvaliacoes()

            _uiState.value = if (faturamento.isSuccess && avaliacoes.isSuccess) {
                _uiState.value.copy(
                    loading = false,
                    faturamento = faturamento.getOrNull(),
                    avaliacoes = avaliacoes.getOrNull(),
                    message = "Dashboard atualizado com sucesso.",
                    isError = false
                )
            } else {
                _uiState.value.copy(
                    loading = false,
                    message = faturamento.exceptionOrNull()?.message
                        ?: avaliacoes.exceptionOrNull()?.message
                        ?: "Falha ao carregar dashboard.",
                    isError = true
                )
            }
        }
    }

    private fun buildHospedeFromForm(): Hospede? {
        val form = _uiState.value.guestForm
        val id = form.idHospede.toIntOrNull()
        if (id == null || id <= 0 || form.nome.isBlank() || form.email.isBlank() || form.cpf.isBlank()) {
            showMessage("Preencha ID, nome, e-mail e CPF antes de salvar.", true)
            return null
        }

        return Hospede(
            idHospede = id,
            nome = form.nome.trim(),
            email = form.email.trim(),
            cpf = form.cpf.trim(),
            telefone = form.telefone.trim().ifBlank { null },
            dataNascimento = form.dataNascimento.trim().ifBlank { null },
            ativo = form.ativo
        )
    }

    private fun buildReservaFromForm(): Reserva? {
        val form = _uiState.value.reservaForm
        val idReserva = form.idReserva.toIntOrNull()
        val idHospede = form.idHospede.toIntOrNull()
        val idQuarto = form.idQuarto.toIntOrNull()
        val valorPrevisto = form.valorPrevisto.toDoubleOrNull()

        if (
            idReserva == null || idHospede == null || idQuarto == null || valorPrevisto == null ||
            form.dataCheckin.isBlank() || form.dataCheckout.isBlank() || form.status.isBlank() || form.canal.isBlank()
        ) {
            showMessage("Preencha todos os campos da reserva com valores validos.", true)
            return null
        }

        return Reserva(
            idReserva = idReserva,
            idHospede = idHospede,
            idQuarto = idQuarto,
            dataReserva = LocalDateTime.now().toString(),
            dataCheckin = form.dataCheckin.trim(),
            dataCheckout = form.dataCheckout.trim(),
            status = form.status.trim(),
            canal = form.canal.trim(),
            valorPrevisto = valorPrevisto
        )
    }

    private fun launch(successMessage: String?, block: suspend () -> Result<*>): kotlinx.coroutines.Job {
        return viewModelScope.launch {
            // Updating a StateFlow causes Compose to recompose the visible screen automatically.
            _uiState.value = _uiState.value.copy(loading = true)
            val result = block()
            _uiState.value = if (result.isSuccess) {
                _uiState.value.copy(
                    loading = false,
                    message = successMessage ?: _uiState.value.message,
                    isError = false
                )
            } else {
                _uiState.value.copy(
                    loading = false,
                    message = result.exceptionOrNull()?.message ?: "Falha ao acessar a API.",
                    isError = true
                )
            }
        }
    }

    private fun showMessage(message: String, isError: Boolean) {
        _uiState.value = _uiState.value.copy(message = message, isError = isError)
    }
}
