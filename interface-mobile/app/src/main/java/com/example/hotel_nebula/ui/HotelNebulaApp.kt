package com.example.hotel_nebula.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.hotel_nebula.data.Hospede
import com.example.hotel_nebula.data.Quarto
import com.example.hotel_nebula.viewmodel.AppUiState
import com.example.hotel_nebula.viewmodel.HotelViewModel

private enum class Destination(val route: String, val label: String) {
    Home("home", "Inicio"),
    Guests("guests", "Hospedes"),
    GuestForm("guest-form", "Cadastro"),
    Reservations("reservations", "Reservas"),
    Dashboard("dashboard", "Dashboard")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HotelNebulaApp(viewModel: HotelViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val navController = rememberNavController()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.message) {
        if (uiState.message.isNotBlank()) snackbarHostState.showSnackbar(uiState.message)
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Hotel Nebula") }) },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            val backStack by navController.currentBackStackEntryAsState()
            val currentDestination = backStack?.destination
            NavigationBar {
                Destination.entries.forEach { destination ->
                    NavigationBarItem(
                        selected = currentDestination?.hierarchy?.any { it.route == destination.route } == true,
                        onClick = { navController.navigate(destination.route) { launchSingleTop = true } },
                        icon = {},
                        label = { Text(destination.label) }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Destination.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Destination.Home.route) { HomeScreen(uiState, viewModel::salvarBaseUrl) }
            composable(Destination.Guests.route) {
                GuestsScreen(
                    uiState = uiState,
                    onRefresh = viewModel::carregarHospedes,
                    onSearch = viewModel::buscarHospedePorId,
                    onDelete = viewModel::excluirHospede
                )
            }
            composable(Destination.GuestForm.route) {
                GuestFormScreen(
                    uiState = uiState,
                    onFormChange = viewModel::atualizarGuestForm,
                    onCreate = viewModel::criarHospede,
                    onUpdate = viewModel::atualizarHospede
                )
            }
            composable(Destination.Reservations.route) {
                ReservationsScreen(
                    uiState = uiState,
                    onFormChange = viewModel::atualizarReservaForm,
                    onRefreshRooms = viewModel::carregarQuartos,
                    onCreateReservation = viewModel::criarReserva
                )
            }
            composable(Destination.Dashboard.route) {
                DashboardScreen(uiState = uiState, onRefresh = viewModel::carregarDashboard)
            }
        }
    }
}

@Composable
private fun HomeScreen(uiState: AppUiState, onSaveBaseUrl: (String) -> Unit) {
    var baseUrl by remember(uiState.baseUrl) { mutableStateOf(uiState.baseUrl) }

    ScreenColumn {
        Text("Configuracao da API", style = MaterialTheme.typography.headlineSmall)
        Text("No emulador Android, 10.0.2.2 aponta para o localhost do computador.")
        OutlinedTextField(
            value = baseUrl,
            onValueChange = { baseUrl = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Base URL") },
            singleLine = true
        )
        Button(onClick = { onSaveBaseUrl(baseUrl) }, modifier = Modifier.fillMaxWidth()) {
            Text("Salvar configuracao")
        }
        StatusText(uiState)
    }
}

@Composable
private fun GuestsScreen(
    uiState: AppUiState,
    onRefresh: () -> Unit,
    onSearch: (String) -> Unit,
    onDelete: (Int) -> Unit
) {
    var searchId by remember { mutableStateOf("") }

    ScreenColumn {
        Text("Buscar hospedes", style = MaterialTheme.typography.headlineSmall)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                value = searchId,
                onValueChange = { searchId = it },
                modifier = Modifier.weight(1f),
                label = { Text("ID do hospede") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true
            )
            Button(onClick = { onSearch(searchId) }) { Text("Buscar") }
        }

        uiState.hospedeEncontrado?.let { GuestCard(it, onDelete = null) }

        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            Text("Lista de hospedes", style = MaterialTheme.typography.titleMedium)
            OutlinedButton(onClick = onRefresh) { Text("Recarregar") }
        }

        StatusText(uiState)
        if (uiState.loading) CircularProgressIndicator()
        if (uiState.hospedes.isEmpty() && !uiState.loading) {
            Text("Nenhum hospede encontrado.")
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.height(420.dp)
            ) {
                items(uiState.hospedes, key = { it.idHospede }) { hospede ->
                    GuestCard(hospede = hospede, onDelete = { onDelete(hospede.idHospede) })
                }
            }
        }
    }
}

@Composable
private fun GuestFormScreen(
    uiState: AppUiState,
    onFormChange: (com.example.hotel_nebula.viewmodel.GuestFormState.() -> com.example.hotel_nebula.viewmodel.GuestFormState) -> Unit,
    onCreate: () -> Unit,
    onUpdate: () -> Unit
) {
    val form = uiState.guestForm

    ScreenColumn {
        Text("Cadastrar ou atualizar hospede", style = MaterialTheme.typography.headlineSmall)
        NumberField("ID", form.idHospede) { onFormChange { copy(idHospede = it) } }
        TextField("Nome", form.nome) { onFormChange { copy(nome = it) } }
        TextField("E-mail", form.email) { onFormChange { copy(email = it) } }
        TextField("CPF", form.cpf) { onFormChange { copy(cpf = it) } }
        TextField("Telefone", form.telefone) { onFormChange { copy(telefone = it) } }
        TextField("Data de nascimento (AAAA-MM-DD)", form.dataNascimento) {
            onFormChange { copy(dataNascimento = it) }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            FilterChip(
                selected = form.ativo,
                onClick = { onFormChange { copy(ativo = true) } },
                label = { Text("Ativo") }
            )
            FilterChip(
                selected = !form.ativo,
                onClick = { onFormChange { copy(ativo = false) } },
                label = { Text("Inativo") }
            )
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
            Button(onClick = onCreate, modifier = Modifier.weight(1f)) { Text("Criar") }
            OutlinedButton(onClick = onUpdate, modifier = Modifier.weight(1f)) { Text("Atualizar") }
        }
        StatusText(uiState)
    }
}

@Composable
private fun ReservationsScreen(
    uiState: AppUiState,
    onFormChange: (com.example.hotel_nebula.viewmodel.ReservaFormState.() -> com.example.hotel_nebula.viewmodel.ReservaFormState) -> Unit,
    onRefreshRooms: () -> Unit,
    onCreateReservation: () -> Unit
) {
    val form = uiState.reservaForm

    ScreenColumn {
        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            Text("Quartos disponiveis", style = MaterialTheme.typography.headlineSmall)
            OutlinedButton(onClick = onRefreshRooms) { Text("Atualizar") }
        }

        if (uiState.quartos.isEmpty()) {
            Text("Nenhum quarto disponivel.")
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.height(190.dp)
            ) {
                items(uiState.quartos, key = { it.idQuarto }) { quarto ->
                    RoomCard(quarto)
                }
            }
        }

        Text("Criar reserva", style = MaterialTheme.typography.titleLarge)
        NumberField("ID reserva", form.idReserva) { onFormChange { copy(idReserva = it) } }
        NumberField("ID hospede", form.idHospede) { onFormChange { copy(idHospede = it) } }
        NumberField("ID quarto", form.idQuarto) { onFormChange { copy(idQuarto = it) } }
        TextField("Check-in (AAAA-MM-DD)", form.dataCheckin) { onFormChange { copy(dataCheckin = it) } }
        TextField("Check-out (AAAA-MM-DD)", form.dataCheckout) { onFormChange { copy(dataCheckout = it) } }
        TextField("Status", form.status) { onFormChange { copy(status = it) } }
        TextField("Canal", form.canal) { onFormChange { copy(canal = it) } }
        NumberField("Valor previsto", form.valorPrevisto) { onFormChange { copy(valorPrevisto = it) } }
        Button(onClick = onCreateReservation, modifier = Modifier.fillMaxWidth()) {
            Text("Salvar reserva")
        }
        StatusText(uiState)
    }
}

@Composable
private fun DashboardScreen(uiState: AppUiState, onRefresh: () -> Unit) {
    ScreenColumn {
        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            Text("Dashboard", style = MaterialTheme.typography.headlineSmall)
            OutlinedButton(onClick = onRefresh) { Text("Atualizar") }
        }
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Faturamento", style = MaterialTheme.typography.titleMedium)
                Text("Total de hospedagens: ${uiState.faturamento?.totalHospedagens ?: "-"}")
                Text("Faturamento total: R$ ${uiState.faturamento?.faturamentoTotal ?: "-"}")
            }
        }
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Avaliacoes", style = MaterialTheme.typography.titleMedium)
                Text("Total de avaliacoes: ${uiState.avaliacoes?.totalAvaliacoes ?: "-"}")
                Text("Media geral: ${uiState.avaliacoes?.mediaGeral ?: "-"}")
            }
        }
        StatusText(uiState)
        if (uiState.loading) CircularProgressIndicator()
    }
}

@Composable
private fun GuestCard(hospede: Hospede, onDelete: (() -> Unit)?) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text("${hospede.idHospede} - ${hospede.nome}", style = MaterialTheme.typography.titleMedium)
            Text(hospede.email)
            Text("CPF: ${hospede.cpf}")
            Text("Ativo: ${if (hospede.ativo) "Sim" else "Nao"}")
            if (onDelete != null) {
                OutlinedButton(onClick = onDelete, modifier = Modifier.fillMaxWidth()) {
                    Text("Excluir")
                }
            }
        }
    }
}

@Composable
private fun RoomCard(quarto: Quarto) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text("ID ${quarto.idQuarto} - Quarto ${quarto.numero}", style = MaterialTheme.typography.titleMedium)
            Text("${quarto.tipo} - capacidade ${quarto.capacidade}")
            Text("R$ ${quarto.precoDiaria} - ${quarto.status}")
        }
    }
}

@Composable
private fun ScreenColumn(content: @Composable ColumnScope.() -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        content = content
    )
}

@Composable
private fun StatusText(uiState: AppUiState) {
    if (uiState.message.isNotBlank()) {
        Text(
            text = uiState.message,
            color = if (uiState.isError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
private fun TextField(label: String, value: String, onChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = onChange,
        modifier = Modifier.fillMaxWidth(),
        label = { Text(label) },
        singleLine = true
    )
}

@Composable
private fun NumberField(label: String, value: String, onChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = onChange,
        modifier = Modifier.fillMaxWidth(),
        label = { Text(label) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        singleLine = true
    )
}
