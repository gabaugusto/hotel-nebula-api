package com.nebulahotel.repository;

import com.nebulahotel.model.Hospedagens;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.Optional;

public interface HospedagensRepository extends JpaRepository<Hospedagens, Integer> {

    Optional<Hospedagens> findByIdReserva(Integer idReserva);

    @Query("SELECT COALESCE(SUM(h.totalGeral), 0) FROM Hospedagens h WHERE LOWER(h.status) = 'encerrada'")
    BigDecimal calcularFaturamentoTotal();
}
