package com.nebulahotel.controller;

import com.nebulahotel.model.Servicos;
import com.nebulahotel.repository.ServicosRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/servicos")
public class ServicosController {

    private final ServicosRepository servicosRepository;

    public ServicosController(ServicosRepository servicosRepository) {
        this.servicosRepository = servicosRepository;
    }

    @GetMapping
    public List<Servicos> listarTodos() {
        return servicosRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Servicos> buscarPorId(@PathVariable Integer id) {
        return servicosRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/disponiveis")
    public List<Servicos> listarDisponiveis() {
        return servicosRepository.findByDisponivelTrue();
    }

    @PostMapping
    public ResponseEntity<?> criar(@RequestBody Servicos servico) {
        if (servico.getIdServico() == null) {
            return ResponseEntity.badRequest().body(Map.of("erro", "O campo idServico é obrigatório."));
        }

        if (servicosRepository.existsById(servico.getIdServico())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("erro", "Já existe serviço com este idServico."));
        }

        if (servico.getDisponivel() == null) {
            servico.setDisponivel(true);
        }

        Servicos salvo = servicosRepository.save(servico);
        return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Servicos> atualizar(@PathVariable Integer id, @RequestBody Servicos servicoAtualizado) {
        if (!servicosRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        servicoAtualizado.setIdServico(id);
        Servicos salvo = servicosRepository.save(servicoAtualizado);
        return ResponseEntity.ok(salvo);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Integer id) {
        if (!servicosRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        servicosRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
