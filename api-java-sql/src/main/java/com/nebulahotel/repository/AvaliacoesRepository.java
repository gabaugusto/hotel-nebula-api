package com.nebulahotel.repository;

import com.nebulahotel.model.Avaliacoes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;

public interface AvaliacoesRepository extends JpaRepository<Avaliacoes, Integer> {

    List<Avaliacoes> findByIdHospedeOrderByDataAvaliacaoDesc(Integer idHospede);

    @Query("SELECT COALESCE(AVG(a.notaGeral), 0) FROM Avaliacoes a")
    BigDecimal calcularMediaGeral();
}
