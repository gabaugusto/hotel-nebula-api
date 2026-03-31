package com.nebulahotel.repository;

import com.nebulahotel.model.Quartos;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface QuartosRepository extends JpaRepository<Quartos, Integer> {

    List<Quartos> findByStatusIgnoreCase(String status);

    boolean existsByNumero(String numero);

    Optional<Quartos> findByNumero(String numero);
}
