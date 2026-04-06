package com.nebulahotel.controller

import com.nebulahotel.model.Hospedagens
import com.nebulahotel.repository.HospedagensRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@CrossOrigin
@RestController
@RequestMapping("/hospedagens")
class HospedagensController(
    private val hospedagensRepository: HospedagensRepository
) {

    @GetMapping
    fun listarTodas(): List<Hospedagens> = hospedagensRepository.findAll()

    @GetMapping("/{id}")
    fun buscarPorId(@PathVariable id: Int): ResponseEntity<Hospedagens> {
        val hospedagem = hospedagensRepository.findById(id)
        return if (hospedagem.isPresent) ResponseEntity.ok(hospedagem.get()) else ResponseEntity.notFound().build<Hospedagens>()
    }

    @PostMapping
    fun criar(@RequestBody hospedagem: Hospedagens): ResponseEntity<Any> {
        if (hospedagensRepository.existsById(hospedagem.idHospedagem)) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(mapOf("erro" to "Já existe hospedagem com este idHospedagem."))
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(hospedagensRepository.save(hospedagem))
    }

    @PutMapping("/{id}")
    fun atualizar(@PathVariable id: Int, @RequestBody hospedagemAtualizada: Hospedagens): ResponseEntity<Hospedagens> {
        if (!hospedagensRepository.existsById(id)) {
            return ResponseEntity.notFound().build<Hospedagens>()
        }
        val salvo = hospedagensRepository.save(hospedagemAtualizada.copy(idHospedagem = id))
        return ResponseEntity.ok(salvo)
    }

    @DeleteMapping("/{id}")
    fun remover(@PathVariable id: Int): ResponseEntity<Void> {
        if (!hospedagensRepository.existsById(id)) {
            return ResponseEntity.notFound().build()
        }
        hospedagensRepository.deleteById(id)
        return ResponseEntity.noContent().build()
    }
}
