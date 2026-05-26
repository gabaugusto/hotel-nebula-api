package com.nebulahotel.repository;

import com.nebulahotel.model.Pagamentos;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PagamentosRepository extends JpaRepository<Pagamentos, Integer> {
}
