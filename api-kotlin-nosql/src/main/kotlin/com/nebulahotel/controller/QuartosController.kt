package com.nebulahotel.controller

import com.nebulahotel.model.Quartos
import com.nebulahotel.repository.QuartosRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@CrossOrigin
@RestController
@RequestMapping("/quartos")
class QuartosController(
    private val quartosRepository: QuartosRepository
) {

    @GetMapping
    fun listarTodos(): List<Quartos> = quartosRepository.findAll()

    @GetMapping("/{id}")
    fun buscarPorId(@PathVariable id: Int): ResponseEntity<Quartos> {
        val quarto = quartosRepository.findById(id)
        return if (quarto.isPresent) ResponseEntity.ok(quarto.get()) else ResponseEntity.notFound().build<Quartos>()
    }

    @GetMapping("/disponiveis")
    fun listarDisponiveis(): List<Quartos> = quartosRepository.findByStatusIgnoreCase("disponivel")

    @PostMapping
    fun criar(@RequestBody quarto: Quartos): ResponseEntity<Any> {
        if (quartosRepository.existsById(quarto.idQuarto)) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(mapOf("erro" to "Já existe quarto com este idQuarto."))
        }
        if (quartosRepository.existsByNumero(quarto.numero)) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(mapOf("erro" to "Já existe quarto com este número."))
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(quartosRepository.save(quarto))
    }

    @PutMapping("/{id}")
    fun atualizar(@PathVariable id: Int, @RequestBody quartoAtualizado: Quartos): ResponseEntity<Any> {
        if (!quartosRepository.existsById(id)) {
            return ResponseEntity.notFound().build()
        }

        val existentePorNumero = quartosRepository.findByNumero(quartoAtualizado.numero)
        if (existentePorNumero != null && existentePorNumero.idQuarto != id) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(mapOf("erro" to "Já existe quarto com este número."))
        }

        val salvo = quartosRepository.save(quartoAtualizado.copy(idQuarto = id))
        return ResponseEntity.ok(salvo)
    }

    @DeleteMapping("/{id}")
    fun remover(@PathVariable id: Int): ResponseEntity<Void> {
        if (!quartosRepository.existsById(id)) {
            return ResponseEntity.notFound().build()
        }
        quartosRepository.deleteById(id)
        return ResponseEntity.noContent().build()
    }
}
