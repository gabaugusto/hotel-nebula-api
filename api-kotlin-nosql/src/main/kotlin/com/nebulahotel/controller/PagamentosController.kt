package com.nebulahotel.controller

import com.nebulahotel.model.Pagamentos
import com.nebulahotel.repository.PagamentosRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@CrossOrigin
@RestController
@RequestMapping("/pagamentos")
class PagamentosController(
    private val pagamentosRepository: PagamentosRepository
) {

    @GetMapping
    fun listarTodos(): List<Pagamentos> = pagamentosRepository.findAll()

    @GetMapping("/{id}")
    fun buscarPorId(@PathVariable id: Int): ResponseEntity<Pagamentos> {
        val pagamento = pagamentosRepository.findById(id)
        return if (pagamento.isPresent) ResponseEntity.ok(pagamento.get()) else ResponseEntity.notFound().build<Pagamentos>()
    }

    @PostMapping
    fun criar(@RequestBody pagamento: Pagamentos): ResponseEntity<Any> {
        if (pagamentosRepository.existsById(pagamento.idPagamento)) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(mapOf("erro" to "Já existe pagamento com este _id."))
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(pagamentosRepository.save(pagamento))
    }

    @PutMapping("/{id}")
    fun atualizar(@PathVariable id: Int, @RequestBody pagamentoAtualizado: Pagamentos): ResponseEntity<Pagamentos> {
        if (!pagamentosRepository.existsById(id)) {
            return ResponseEntity.notFound().build<Pagamentos>()
        }
        val salvo = pagamentosRepository.save(pagamentoAtualizado.copy(idPagamento = id))
        return ResponseEntity.ok(salvo)
    }

    @DeleteMapping("/{id}")
    fun remover(@PathVariable id: Int): ResponseEntity<Void> {
        if (!pagamentosRepository.existsById(id)) {
            return ResponseEntity.notFound().build()
        }
        pagamentosRepository.deleteById(id)
        return ResponseEntity.noContent().build()
    }
}
