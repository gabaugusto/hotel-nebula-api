package com.nebulahotel.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.math.BigDecimal
import java.time.LocalDateTime

@Document(collection = "feedbacks")
data class Avaliacoes(
    @Id
    val idFeedback: Int,
    val idHospede: Int,
    val idQuarto: Int,
    val idHospedagem: Int,
    val notaGeral: BigDecimal,
    val comentario: String? = null,
    val dataAvaliacao: LocalDateTime = LocalDateTime.now()
)
