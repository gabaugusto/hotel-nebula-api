package com.nebulahotel.model

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime

@Document(collection = "reservas")
data class Reservas(
    @Id
    @JsonProperty("_id")
    val idReserva: Int,
    @JsonProperty("hospede_id")
    val idHospede: Int,
    @JsonProperty("quarto_id")
    val idQuarto: Int,
    @JsonProperty("data_reserva")
    val dataReserva: LocalDateTime = LocalDateTime.now(),
    @JsonProperty("data_checkin")
    val dataCheckin: LocalDate,
    @JsonProperty("data_checkout")
    val dataCheckout: LocalDate,
    val status: String,
    val canal: String,
    @JsonProperty("valor_previsto")
    val valorPrevisto: BigDecimal
)
