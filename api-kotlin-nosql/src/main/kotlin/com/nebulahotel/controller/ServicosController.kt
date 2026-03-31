package com.nebulahotel.controller

import com.nebulahotel.model.Servicos
import com.nebulahotel.repository.ServicosRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/servicos")
class ServicosController(
    private val servicosRepository: ServicosRepository
) {

    @GetMapping
    fun listarTodos(): List<Servicos> = servicosRepository.findAll()

    @GetMapping("/{id}")
    fun buscarPorId(@PathVariable id: Int): ResponseEntity<Servicos> {
        val servico = servicosRepository.findById(id)
        return if (servico.isPresent) ResponseEntity.ok(servico.get()) else ResponseEntity.notFound().build<Servicos>()
    }

    @GetMapping("/disponiveis")
    fun listarDisponiveis(): List<Servicos> = servicosRepository.findByDisponivelTrue()

    @PostMapping
    fun criar(@RequestBody servico: Servicos): ResponseEntity<Any> {
        if (servicosRepository.existsById(servico.idServico)) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(mapOf("erro" to "Já existe serviço com este idServico."))
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(servicosRepository.save(servico))
    }

    @PutMapping("/{id}")
    fun atualizar(@PathVariable id: Int, @RequestBody servicoAtualizado: Servicos): ResponseEntity<Servicos> {
        if (!servicosRepository.existsById(id)) {
            return ResponseEntity.notFound().build<Servicos>()
        }
        val salvo = servicosRepository.save(servicoAtualizado.copy(idServico = id))
        return ResponseEntity.ok(salvo)
    }

    @DeleteMapping("/{id}")
    fun remover(@PathVariable id: Int): ResponseEntity<Void> {
        if (!servicosRepository.existsById(id)) {
            return ResponseEntity.notFound().build()
        }
        servicosRepository.deleteById(id)
        return ResponseEntity.noContent().build()
    }
}
