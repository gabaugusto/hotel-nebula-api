package com.nebulahotel.repository

import com.nebulahotel.model.Servicos
import org.springframework.data.mongodb.repository.MongoRepository

interface ServicosRepository : MongoRepository<Servicos, Int> {
    fun findByDisponivelTrue(): List<Servicos>
}
