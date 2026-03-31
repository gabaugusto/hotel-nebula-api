package com.nebulahotel.controller;

import com.nebulahotel.repository.AvaliacoesRepository;
import com.nebulahotel.repository.HospedesRepository;
import com.nebulahotel.repository.ReservasRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/hospedes")
public class HistoricoHospedesController {

    private final HospedesRepository hospedesRepository;
    private final ReservasRepository reservasRepository;
    private final AvaliacoesRepository avaliacoesRepository;

    public HistoricoHospedesController(HospedesRepository hospedesRepository,
                                       ReservasRepository reservasRepository,
                                       AvaliacoesRepository avaliacoesRepository) {
        this.hospedesRepository = hospedesRepository;
        this.reservasRepository = reservasRepository;
        this.avaliacoesRepository = avaliacoesRepository;
    }

    @GetMapping("/historico/{id}")
    public ResponseEntity<?> historico(@PathVariable Integer id) {
        return hospedesRepository.findById(id)
                .<ResponseEntity<?>>map(hospede -> ResponseEntity.ok(Map.of(
                        "hospede", hospede,
                        "reservas", reservasRepository.findByIdHospedeOrderByDataReservaDesc(id),
                        "avaliacoes", avaliacoesRepository.findByIdHospedeOrderByDataAvaliacaoDesc(id)
                )))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
