package com.nebulahotel.model

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.math.BigDecimal
import java.time.LocalDateTime

@Document(collection = "hospedagens")
data class Hospedagens(
    @Id
    @JsonProperty("_id")
    val idHospedagem: Int,
    @JsonProperty("reserva_id")
    val idReserva: Int,
    val idFuncionarioCheckin: Int? = null,
    val idFuncionarioCheckout: Int? = null,
    @JsonProperty("checkin_real")
    val checkinReal: LocalDateTime? = null,
    @JsonProperty("checkout_real")
    val checkoutReal: LocalDateTime? = null,
    val status: String,
    @JsonProperty("total_diarias")
    val totalDiarias: BigDecimal,
    @JsonProperty("total_servicos")
    val totalServicos: BigDecimal,
    @JsonProperty("total_geral")
    val totalGeral: BigDecimal
)
