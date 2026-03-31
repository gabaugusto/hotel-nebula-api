package com.nebulahotel.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.math.BigDecimal

@Document(collection = "servicos")
data class Servicos(
    @Id
    val idServico: Int,
    val nome: String,
    val categoria: String,
    val preco: BigDecimal,
    val disponivel: Boolean = true
)
