package com.nebulahotel.controller;

import com.nebulahotel.model.Quartos;
import com.nebulahotel.repository.QuartosRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/quartos")
public class QuartosController {

    private final QuartosRepository quartosRepository;

    public QuartosController(QuartosRepository quartosRepository) {
        this.quartosRepository = quartosRepository;
    }

    @GetMapping
    public List<Quartos> listarTodos() {
        return quartosRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Quartos> buscarPorId(@PathVariable Integer id) {
        return quartosRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/disponiveis")
    public List<Quartos> listarDisponiveis() {
        return quartosRepository.findByStatusIgnoreCase("disponivel");
    }

    @PostMapping
    public ResponseEntity<?> criar(@RequestBody Quartos quarto) {
        if (quarto.getIdQuarto() == null) {
            return ResponseEntity.badRequest().body(Map.of("erro", "O campo idQuarto é obrigatório."));
        }

        if (quartosRepository.existsById(quarto.getIdQuarto())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("erro", "Já existe quarto com este idQuarto."));
        }

        if (quarto.getNumero() != null && quartosRepository.existsByNumero(quarto.getNumero())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("erro", "Já existe quarto com este número."));
        }

        Quartos salvo = quartosRepository.save(quarto);
        return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Integer id, @RequestBody Quartos quartoAtualizado) {
        Optional<Quartos> quartoExistenteOpt = quartosRepository.findById(id);
        if (quartoExistenteOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Quartos quartoExistente = quartoExistenteOpt.get();
        if (quartoAtualizado.getNumero() != null && !quartoAtualizado.getNumero().equals(quartoExistente.getNumero())) {
            Optional<Quartos> porNumero = quartosRepository.findByNumero(quartoAtualizado.getNumero());
            if (porNumero.isPresent() && !porNumero.get().getIdQuarto().equals(id)) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(Map.of("erro", "Já existe quarto com este número."));
            }
        }

        quartoAtualizado.setIdQuarto(id);
        Quartos salvo = quartosRepository.save(quartoAtualizado);
        return ResponseEntity.ok(salvo);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Integer id) {
        if (!quartosRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        quartosRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
