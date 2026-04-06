package com.nebulahotel.controller

import com.nebulahotel.model.Hospedes
import com.nebulahotel.repository.HospedesRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@CrossOrigin
@RestController
@RequestMapping("/hospedes")
class HospedesController(
    private val hospedesRepository: HospedesRepository
) {

    @GetMapping
    fun listarTodos(): List<Hospedes> = hospedesRepository.findAll()

    @GetMapping("/{id}")
    fun buscarPorId(@PathVariable id: Int): ResponseEntity<Hospedes> {
        val hospede = hospedesRepository.findById(id)
        return if (hospede.isPresent) ResponseEntity.ok(hospede.get()) else ResponseEntity.notFound().build<Hospedes>()
    }

    @GetMapping("/email/{email}")
    fun buscarPorEmail(@PathVariable email: String): ResponseEntity<Hospedes> {
        val hospede = hospedesRepository.findByEmail(email)
        return if (hospede != null) ResponseEntity.ok(hospede) else ResponseEntity.notFound().build<Hospedes>()
    }

    @PostMapping
    fun criar(@RequestBody hospede: Hospedes): ResponseEntity<Any> {
        if (hospedesRepository.existsById(hospede.idHospede)) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(mapOf("erro" to "Já existe hóspede com este idHospede."))
        }
        if (hospedesRepository.existsByEmail(hospede.email)) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(mapOf("erro" to "Já existe hóspede com este email."))
        }
        if (hospedesRepository.existsByCpf(hospede.cpf)) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(mapOf("erro" to "Já existe hóspede com este CPF."))
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(hospedesRepository.save(hospede))
    }

    @PutMapping("/{id}")
    fun atualizar(@PathVariable id: Int, @RequestBody hospedeAtualizado: Hospedes): ResponseEntity<Any> {
        val existente = hospedesRepository.findById(id)
        if (existente.isEmpty) {
            return ResponseEntity.notFound().build()
        }

        val hospedeExistente = existente.get()
        if (hospedeAtualizado.email != hospedeExistente.email && hospedesRepository.existsByEmail(hospedeAtualizado.email)) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(mapOf("erro" to "Já existe hóspede com este email."))
        }

        if (hospedeAtualizado.cpf != hospedeExistente.cpf && hospedesRepository.existsByCpf(hospedeAtualizado.cpf)) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(mapOf("erro" to "Já existe hóspede com este CPF."))
        }

        val salvo = hospedesRepository.save(
            hospedeAtualizado.copy(
                idHospede = id,
                dataCadastro = hospedeAtualizado.dataCadastro ?: hospedeExistente.dataCadastro,
                ativo = hospedeAtualizado.ativo
            )
        )
        return ResponseEntity.ok(salvo)
    }

    @DeleteMapping("/{id}")
    fun remover(@PathVariable id: Int): ResponseEntity<Void> {
        if (!hospedesRepository.existsById(id)) {
            return ResponseEntity.notFound().build()
        }
        hospedesRepository.deleteById(id)
        return ResponseEntity.noContent().build()
    }
}
