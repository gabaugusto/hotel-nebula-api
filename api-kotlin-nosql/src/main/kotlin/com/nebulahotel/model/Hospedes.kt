package com.nebulahotel.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDate
import java.time.LocalDateTime

@Document(collection = "hospedes")
data class Hospedes(
    @Id
    val idHospede: Int,
    val nome: String,
    val email: String,
    val cpf: String,
    val telefone: String? = null,
    val dataNascimento: LocalDate? = null,
    val dataCadastro: LocalDateTime = LocalDateTime.now(),
    val ativo: Boolean = true
)
