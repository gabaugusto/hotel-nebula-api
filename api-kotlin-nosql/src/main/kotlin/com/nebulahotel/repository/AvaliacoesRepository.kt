package com.nebulahotel.repository

import com.nebulahotel.model.Avaliacoes
import org.springframework.data.mongodb.repository.MongoRepository

interface AvaliacoesRepository : MongoRepository<Avaliacoes, Int> {
    fun findByIdHospedeOrderByDataAvaliacaoDesc(idHospede: Int): List<Avaliacoes>
}
