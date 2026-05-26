package com.nebulahotel.controller;

import com.nebulahotel.model.Pagamentos;
import com.nebulahotel.repository.PagamentosRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/pagamentos")
public class PagamentosController {

    private final PagamentosRepository pagamentosRepository;

    public PagamentosController(PagamentosRepository pagamentosRepository) {
        this.pagamentosRepository = pagamentosRepository;
    }

    @GetMapping
    public List<Pagamentos> listarTodos() {
        return pagamentosRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pagamentos> buscarPorId(@PathVariable Integer id) {
        return pagamentosRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> criar(@RequestBody Pagamentos pagamento) {
        if (pagamento.getIdPagamento() == null) {
            return ResponseEntity.badRequest().body(Map.of("erro", "O campo idPagamento é obrigatório."));
        }

        if (pagamentosRepository.existsById(pagamento.getIdPagamento())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("erro", "Já existe pagamento com este idPagamento."));
        }

        Pagamentos novoPagamento = pagamentosRepository.save(pagamento);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoPagamento);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Integer id, @RequestBody Pagamentos pagamento) {
        Optional<Pagamentos> optional = pagamentosRepository.findById(id);
        if (optional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Pagamentos existente = optional.get();
        if (pagamento.getValor() != null) {
            existente.setValor(pagamento.getValor());
        }
        if (pagamento.getMetodo() != null) {
            existente.setMetodo(pagamento.getMetodo());
        }
        if (pagamento.getDataPagamento() != null) {
            existente.setDataPagamento(pagamento.getDataPagamento());
        }
        if (pagamento.getStatus() != null) {
            existente.setStatus(pagamento.getStatus());
        }
        if (pagamento.getTransacaoId() != null) {
            existente.setTransacaoId(pagamento.getTransacaoId());
        }

        Pagamentos atualizado = pagamentosRepository.save(existente);
        return ResponseEntity.ok(atualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletar(@PathVariable Integer id) {
        if (!pagamentosRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        pagamentosRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
