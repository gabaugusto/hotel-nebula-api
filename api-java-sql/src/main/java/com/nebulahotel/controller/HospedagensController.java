package com.nebulahotel.controller;

import com.nebulahotel.model.Hospedagens;
import com.nebulahotel.repository.HospedagensRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/hospedagens")
public class HospedagensController {

    private final HospedagensRepository hospedagensRepository;

    public HospedagensController(HospedagensRepository hospedagensRepository) {
        this.hospedagensRepository = hospedagensRepository;
    }

    @GetMapping
    public List<Hospedagens> listarTodas() {
        return hospedagensRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Hospedagens> buscarPorId(@PathVariable Integer id) {
        return hospedagensRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> criar(@RequestBody Hospedagens hospedagem) {
        if (hospedagem.getIdHospedagem() == null) {
            return ResponseEntity.badRequest().body(Map.of("erro", "O campo idHospedagem é obrigatório."));
        }

        if (hospedagensRepository.existsById(hospedagem.getIdHospedagem())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("erro", "Já existe hospedagem com este idHospedagem."));
        }

        Hospedagens salvo = hospedagensRepository.save(hospedagem);
        return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Hospedagens> atualizar(@PathVariable Integer id, @RequestBody Hospedagens hospedagemAtualizada) {
        if (!hospedagensRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        hospedagemAtualizada.setIdHospedagem(id);
        Hospedagens salvo = hospedagensRepository.save(hospedagemAtualizada);
        return ResponseEntity.ok(salvo);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Integer id) {
        if (!hospedagensRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        hospedagensRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
