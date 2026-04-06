package com.nebulahotel.controller

import com.nebulahotel.repository.HospedagensRepository
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.math.BigDecimal

@CrossOrigin
@RestController
@RequestMapping("/dashboard")
class DashboardController(
    private val hospedagensRepository: HospedagensRepository
) {

    @GetMapping("/faturamento")
    fun faturamento(): Map<String, Any> {
        val hospedagens = hospedagensRepository.findAll()
        val total = hospedagens.fold(BigDecimal.ZERO) { acc, item -> acc + item.totalGeral }

        return mapOf(
            "totalHospedagens" to hospedagens.size,
            "faturamentoTotal" to total
        )
    }
}
