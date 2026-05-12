package com.nebulahotel.model

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.math.BigDecimal

@Document(collection = "quartos")
data class Quartos(
    @Id
    @JsonProperty("_id")
    val idQuarto: Int,
    val numero: String,
    val tipo: String,
    val capacidade: Int,
    @JsonProperty("preco_diaria")
    val precoDiaria: BigDecimal,
    val status: String,
    val andar: Int? = null,
    val vista: String? = null
)
