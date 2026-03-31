package com.nebulahotel.repository

import com.nebulahotel.model.Hospedes
import org.springframework.data.mongodb.repository.MongoRepository

interface HospedesRepository : MongoRepository<Hospedes, Int> {
    fun findByEmail(email: String): Hospedes?
    fun existsByEmail(email: String): Boolean
    fun existsByCpf(cpf: String): Boolean
}
