package com.nebulahotel.controller

import com.nebulahotel.model.Avaliacoes
import com.nebulahotel.repository.AvaliacoesRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.math.BigDecimal
import java.math.RoundingMode

@CrossOrigin
@RestController
@RequestMapping("/avaliacoes")
class AvaliacoesController(
    private val avaliacoesRepository: AvaliacoesRepository
) {

    @GetMapping
    fun listarTodas(): List<Avaliacoes> = avaliacoesRepository.findAll()

    @GetMapping("/{id}")
    fun buscarPorId(@PathVariable id: Int): ResponseEntity<Avaliacoes> {
        val avaliacao = avaliacoesRepository.findById(id)
        return if (avaliacao.isPresent) ResponseEntity.ok(avaliacao.get()) else ResponseEntity.notFound().build<Avaliacoes>()
    }

    @GetMapping("/resumo")
    fun resumoAvaliacoes(): Map<String, Any> {
        val avaliacoes = avaliacoesRepository.findAll()
        val total = avaliacoes.size
        val media = if (total == 0) {
            BigDecimal.ZERO
        } else {
            avaliacoes
                .map { it.notaGeral }
                .reduce(BigDecimal::add)
                .divide(BigDecimal(total), 2, RoundingMode.HALF_UP)
        }

        return mapOf(
            "totalAvaliacoes" to total,
            "mediaGeral" to media
        )
    }

    @PostMapping
    fun criar(@RequestBody avaliacao: Avaliacoes): ResponseEntity<Any> {
        if (avaliacoesRepository.existsById(avaliacao.idFeedback)) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(mapOf("erro" to "Já existe avaliação com este idFeedback."))
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(avaliacoesRepository.save(avaliacao))
    }

    @PutMapping("/{id}")
    fun atualizar(@PathVariable id: Int, @RequestBody avaliacaoAtualizada: Avaliacoes): ResponseEntity<Avaliacoes> {
        if (!avaliacoesRepository.existsById(id)) {
            return ResponseEntity.notFound().build<Avaliacoes>()
        }
        val salvo = avaliacoesRepository.save(avaliacaoAtualizada.copy(idFeedback = id))
        return ResponseEntity.ok(salvo)
    }

    @DeleteMapping("/{id}")
    fun remover(@PathVariable id: Int): ResponseEntity<Void> {
        if (!avaliacoesRepository.existsById(id)) {
            return ResponseEntity.notFound().build()
        }
        avaliacoesRepository.deleteById(id)
        return ResponseEntity.noContent().build()
    }
}
