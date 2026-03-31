package com.nebulahotel.repository

import com.nebulahotel.model.Hospedagens
import org.springframework.data.mongodb.repository.MongoRepository

interface HospedagensRepository : MongoRepository<Hospedagens, Int> {
    fun findByIdReserva(idReserva: Int): Hospedagens?
}
