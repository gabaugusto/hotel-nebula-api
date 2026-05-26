package com.nebulahotel.model

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.math.BigDecimal
import java.time.LocalDateTime

@Document(collection = "feedbacks")
data class Avaliacoes(
    @Id
    @JsonProperty("_id")
    val idFeedback: Int,
    @JsonProperty("hospede_id")
    val idHospede: Int,
    @JsonProperty("quarto_id")
    val idQuarto: Int,
    @JsonProperty("hospedagem_id")
    val idHospedagem: Int,
    @JsonProperty("nota_geral")
    val notaGeral: BigDecimal,
    val comentario: String? = null,
    @JsonProperty("data_avaliacao")
    val dataAvaliacao: LocalDateTime = LocalDateTime.now()
)
