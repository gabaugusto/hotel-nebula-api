package com.nebulahotel.controller

import com.nebulahotel.model.Reservas
import com.nebulahotel.repository.ReservasRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime

@RestController
@RequestMapping("/reservas")
class ReservasController(
    private val reservasRepository: ReservasRepository
) {

    @GetMapping
    fun listarTodas(): List<Reservas> = reservasRepository.findAll()

    @GetMapping("/{id}")
    fun buscarPorId(@PathVariable id: Int): ResponseEntity<Reservas> {
        val reserva = reservasRepository.findById(id)
        return if (reserva.isPresent) ResponseEntity.ok(reserva.get()) else ResponseEntity.notFound().build<Reservas>()
    }

    @GetMapping("/ativas")
    fun listarAtivas(): List<Reservas> = reservasRepository.findByStatusIn(listOf("confirmada", "ativa", "em_andamento"))

    @PostMapping
    fun criar(@RequestBody reserva: Reservas): ResponseEntity<Any> {
        if (reservasRepository.existsById(reserva.idReserva)) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(mapOf("erro" to "Já existe reserva com este idReserva."))
        }

        val salvo = reservasRepository.save(
            reserva.copy(dataReserva = reserva.dataReserva ?: LocalDateTime.now())
        )
        return ResponseEntity.status(HttpStatus.CREATED).body(salvo)
    }

    @PutMapping("/{id}")
    fun atualizar(@PathVariable id: Int, @RequestBody reservaAtualizada: Reservas): ResponseEntity<Reservas> {
        if (!reservasRepository.existsById(id)) {
            return ResponseEntity.notFound().build<Reservas>()
        }
        val salvo = reservasRepository.save(reservaAtualizada.copy(idReserva = id))
        return ResponseEntity.ok(salvo)
    }

    @DeleteMapping("/{id}")
    fun remover(@PathVariable id: Int): ResponseEntity<Void> {
        if (!reservasRepository.existsById(id)) {
            return ResponseEntity.notFound().build()
        }
        reservasRepository.deleteById(id)
        return ResponseEntity.noContent().build()
    }
}
