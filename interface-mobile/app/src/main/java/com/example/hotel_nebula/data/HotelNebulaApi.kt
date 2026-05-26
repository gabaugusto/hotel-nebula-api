package com.example.hotel_nebula.data

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface HotelNebulaApi {
    @GET("hospedes")
    suspend fun listarHospedes(): Response<List<Hospede>>

    @GET("hospedes/{id}")
    suspend fun buscarHospede(@Path("id") id: Int): Response<Hospede>

    @POST("hospedes")
    suspend fun criarHospede(@Body hospede: Hospede): Response<Hospede>

    @PUT("hospedes/{id}")
    suspend fun atualizarHospede(
        @Path("id") id: Int,
        @Body hospede: Hospede
    ): Response<Hospede>

    @DELETE("hospedes/{id}")
    suspend fun excluirHospede(@Path("id") id: Int): Response<Void>

    @GET("quartos/disponiveis")
    suspend fun listarQuartosDisponiveis(): Response<List<Quarto>>

    @POST("reservas")
    suspend fun criarReserva(@Body reserva: Reserva): Response<Reserva>

    @GET("dashboard/faturamento")
    suspend fun buscarFaturamento(): Response<DashboardFaturamento>

    @GET("avaliacoes/resumo")
    suspend fun buscarResumoAvaliacoes(): Response<AvaliacoesResumo>
}
