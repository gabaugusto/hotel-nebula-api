package com.nebulahotel.model

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.math.BigDecimal
import java.time.LocalDateTime

@Document(collection = "pagamentos")
data class Pagamentos(
    @Id
    @JsonProperty("_id")
    val idPagamento: Int,
    @JsonProperty("hospedagem_id")
    val idHospedagem: Int,
    val valor: BigDecimal,
    val metodo: String,
    val status: String,
    @JsonProperty("data_pagamento")
    val dataPagamento: LocalDateTime? = null,
    @JsonProperty("transacao_id")
    val transacaoId: String? = null
)
