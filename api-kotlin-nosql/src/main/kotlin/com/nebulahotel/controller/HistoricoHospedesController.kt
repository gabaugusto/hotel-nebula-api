package com.nebulahotel.controller

import com.nebulahotel.repository.AvaliacoesRepository
import com.nebulahotel.repository.HospedesRepository
import com.nebulahotel.repository.ReservasRepository
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/hospedes")
class HistoricoHospedesController(
    private val hospedesRepository: HospedesRepository,
    private val reservasRepository: ReservasRepository,
    private val avaliacoesRepository: AvaliacoesRepository
) {

    @GetMapping("/historico/{id}")
    fun historico(@PathVariable id: Int): ResponseEntity<Any> {
        val hospede = hospedesRepository.findById(id)
        if (hospede.isEmpty) {
            return ResponseEntity.notFound().build()
        }

        return ResponseEntity.ok(
            mapOf(
                "hospede" to hospede.get(),
                "reservas" to reservasRepository.findByIdHospedeOrderByDataReservaDesc(id),
                "avaliacoes" to avaliacoesRepository.findByIdHospedeOrderByDataAvaliacaoDesc(id)
            )
        )
    }
}
