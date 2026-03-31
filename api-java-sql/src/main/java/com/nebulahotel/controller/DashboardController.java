package com.nebulahotel.controller;

import com.nebulahotel.repository.HospedagensRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    private final HospedagensRepository hospedagensRepository;

    public DashboardController(HospedagensRepository hospedagensRepository) {
        this.hospedagensRepository = hospedagensRepository;
    }

    @GetMapping("/faturamento")
    public Map<String, Object> faturamento() {
        return Map.of(
                "totalHospedagens", hospedagensRepository.count(),
                "faturamentoTotal", hospedagensRepository.calcularFaturamentoTotal()
        );
    }
}
