package com.example.hotel_nebula.data

data class Hospede(
    val idHospede: Int,
    val nome: String,
    val email: String,
    val cpf: String,
    val telefone: String? = null,
    val dataNascimento: String? = null,
    val ativo: Boolean = true
)

data class Quarto(
    val idQuarto: Int,
    val numero: String,
    val tipo: String,
    val capacidade: Int,
    val precoDiaria: Double,
    val status: String,
    val andar: Int? = null,
    val vista: String? = null
)

data class Reserva(
    val idReserva: Int,
    val idHospede: Int,
    val idQuarto: Int,
    val dataReserva: String,
    val dataCheckin: String,
    val dataCheckout: String,
    val status: String,
    val canal: String,
    val valorPrevisto: Double
)

data class DashboardFaturamento(
    val totalHospedagens: Int = 0,
    val faturamentoTotal: Double = 0.0
)

data class AvaliacoesResumo(
    val totalAvaliacoes: Int = 0,
    val mediaGeral: Double = 0.0
)
