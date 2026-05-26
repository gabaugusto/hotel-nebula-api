package com.example.hotel_nebula.data

import retrofit2.Response
import java.io.IOException
import java.net.SocketTimeoutException

class HotelRepository(baseUrl: String) {
    private val api = ApiClient.create(baseUrl)

    suspend fun listarHospedes() = call { api.listarHospedes() }
    suspend fun buscarHospede(id: Int) = call { api.buscarHospede(id) }
    suspend fun criarHospede(hospede: Hospede) = call { api.criarHospede(hospede) }
    suspend fun atualizarHospede(id: Int, hospede: Hospede) = call { api.atualizarHospede(id, hospede) }
    suspend fun excluirHospede(id: Int) = callUnit { api.excluirHospede(id) }
    suspend fun listarQuartosDisponiveis() = call { api.listarQuartosDisponiveis() }
    suspend fun criarReserva(reserva: Reserva) = call { api.criarReserva(reserva) }
    suspend fun buscarFaturamento() = call { api.buscarFaturamento() }
    suspend fun buscarResumoAvaliacoes() = call { api.buscarResumoAvaliacoes() }

    // The repository is the translation layer between Retrofit and the UI.
    // Screens receive Result<T> with friendly messages instead of raw exceptions.
    private suspend fun <T> call(request: suspend () -> Response<T>): Result<T> {
        return try {
            val response = request()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) Result.success(body) else Result.failure(Exception("Resposta vazia da API."))
            } else {
                val detail = response.errorBody()?.string()?.takeIf { it.isNotBlank() }
                Result.failure(Exception("Erro HTTP ${response.code()}${detail?.let { ": $it" } ?: ""}"))
            }
        } catch (error: SocketTimeoutException) {
            Result.failure(Exception("Tempo de conexao esgotado. Verifique se a API esta respondendo."))
        } catch (error: IOException) {
            Result.failure(Exception("Nao foi possivel conectar na API. Confira a URL e se o servidor esta ligado."))
        } catch (error: Exception) {
            Result.failure(Exception(error.message ?: "Erro inesperado ao acessar a API."))
        }
    }

    private suspend fun callUnit(request: suspend () -> Response<Void>): Result<Unit> {
        return try {
            val response = request()
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                val detail = response.errorBody()?.string()?.takeIf { it.isNotBlank() }
                Result.failure(Exception("Erro HTTP ${response.code()}${detail?.let { ": $it" } ?: ""}"))
            }
        } catch (error: SocketTimeoutException) {
            Result.failure(Exception("Tempo de conexao esgotado. Verifique se a API esta respondendo."))
        } catch (error: IOException) {
            Result.failure(Exception("Nao foi possivel conectar na API. Confira a URL e se o servidor esta ligado."))
        } catch (error: Exception) {
            Result.failure(Exception(error.message ?: "Erro inesperado ao acessar a API."))
        }
    }
}
