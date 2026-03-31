package com.nebulahotel.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime

@Document(collection = "reservas")
data class Reservas(
    @Id
    val idReserva: Int,
    val idHospede: Int,
    val idQuarto: Int,
    val dataReserva: LocalDateTime = LocalDateTime.now(),
    val dataCheckin: LocalDate,
    val dataCheckout: LocalDate,
    val status: String,
    val canal: String,
    val valorPrevisto: BigDecimal
)
