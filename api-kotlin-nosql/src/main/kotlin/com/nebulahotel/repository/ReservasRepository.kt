package com.nebulahotel.repository

import com.nebulahotel.model.Reservas
import org.springframework.data.mongodb.repository.MongoRepository

interface ReservasRepository : MongoRepository<Reservas, Int> {
    fun findByStatusIn(status: List<String>): List<Reservas>
    fun findByIdHospedeOrderByDataReservaDesc(idHospede: Int): List<Reservas>
}
