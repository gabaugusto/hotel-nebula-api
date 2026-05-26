package com.nebulahotel.controller;

import com.nebulahotel.model.Reservas;
import com.nebulahotel.repository.ReservasRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/reservas")
public class ReservasController {

    private final ReservasRepository reservasRepository;

    public ReservasController(ReservasRepository reservasRepository) {
        this.reservasRepository = reservasRepository;
    }

    @GetMapping
    public List<Reservas> listarTodas() {
        return reservasRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Reservas> buscarPorId(@PathVariable Integer id) {
        return reservasRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/ativas")
    public List<Reservas> listarAtivas() {
        return reservasRepository.findReservasAtivas();
    }

    @PostMapping
    public ResponseEntity<?> criar(@RequestBody Reservas reserva) {
        if (reserva.getIdReserva() == null) {
            return ResponseEntity.badRequest().body(Map.of("erro", "O campo idReserva é obrigatório."));
        }

        if (reservasRepository.existsById(reserva.getIdReserva())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("erro", "Já existe reserva com este idReserva."));
        }

        if (reserva.getDataReserva() == null) {
            reserva.setDataReserva(LocalDateTime.now());
        }

        Reservas salvo = reservasRepository.save(reserva);
        return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Reservas> atualizar(@PathVariable Integer id, @RequestBody Reservas reservaAtualizada) {
        if (!reservasRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        reservaAtualizada.setIdReserva(id);
        Reservas salvo = reservasRepository.save(reservaAtualizada);
        return ResponseEntity.ok(salvo);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Integer id) {
        if (!reservasRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        reservasRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
