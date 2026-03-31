package com.nebulahotel.repository

import com.nebulahotel.model.Quartos
import org.springframework.data.mongodb.repository.MongoRepository

interface QuartosRepository : MongoRepository<Quartos, Int> {
    fun findByStatusIgnoreCase(status: String): List<Quartos>
    fun existsByNumero(numero: String): Boolean
    fun findByNumero(numero: String): Quartos?
}
