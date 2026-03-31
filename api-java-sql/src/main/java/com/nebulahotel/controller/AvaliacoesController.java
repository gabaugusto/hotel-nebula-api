package com.nebulahotel.controller;

import com.nebulahotel.model.Avaliacoes;
import com.nebulahotel.repository.AvaliacoesRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/avaliacoes")
public class AvaliacoesController {

    private final AvaliacoesRepository avaliacoesRepository;

    public AvaliacoesController(AvaliacoesRepository avaliacoesRepository) {
        this.avaliacoesRepository = avaliacoesRepository;
    }

    @GetMapping
    public List<Avaliacoes> listarTodas() {
        return avaliacoesRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Avaliacoes> buscarPorId(@PathVariable Integer id) {
        return avaliacoesRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/resumo")
    public Map<String, Object> resumoAvaliacoes() {
        return Map.of(
                "totalAvaliacoes", avaliacoesRepository.count(),
                "mediaGeral", avaliacoesRepository.calcularMediaGeral()
        );
    }

    @PostMapping
    public ResponseEntity<?> criar(@RequestBody Avaliacoes avaliacao) {
        if (avaliacao.getIdFeedback() == null) {
            return ResponseEntity.badRequest().body(Map.of("erro", "O campo idFeedback é obrigatório."));
        }

        if (avaliacoesRepository.existsById(avaliacao.getIdFeedback())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("erro", "Já existe avaliação com este idFeedback."));
        }

        if (avaliacao.getDataAvaliacao() == null) {
            avaliacao.setDataAvaliacao(LocalDateTime.now());
        }

        Avaliacoes salvo = avaliacoesRepository.save(avaliacao);
        return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Avaliacoes> atualizar(@PathVariable Integer id, @RequestBody Avaliacoes avaliacaoAtualizada) {
        if (!avaliacoesRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        avaliacaoAtualizada.setIdFeedback(id);
        Avaliacoes salvo = avaliacoesRepository.save(avaliacaoAtualizada);
        return ResponseEntity.ok(salvo);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Integer id) {
        if (!avaliacoesRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        avaliacoesRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
