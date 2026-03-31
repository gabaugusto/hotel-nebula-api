package com.nebulahotel.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.math.BigDecimal
import java.time.LocalDateTime

@Document(collection = "hospedagens")
data class Hospedagens(
    @Id
    val idHospedagem: Int,
    val idReserva: Int,
    val idFuncionarioCheckin: Int? = null,
    val idFuncionarioCheckout: Int? = null,
    val checkinReal: LocalDateTime? = null,
    val checkoutReal: LocalDateTime? = null,
    val status: String,
    val totalDiarias: BigDecimal,
    val totalServicos: BigDecimal,
    val totalGeral: BigDecimal
)
