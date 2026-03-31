package com.nebulahotel.repository;

import com.nebulahotel.model.Servicos;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ServicosRepository extends JpaRepository<Servicos, Integer> {

    List<Servicos> findByDisponivelTrue();
}
