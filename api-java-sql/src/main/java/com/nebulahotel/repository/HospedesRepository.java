package com.nebulahotel.repository;

import com.nebulahotel.model.Hospedes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HospedesRepository extends JpaRepository<Hospedes, Integer> {

	Optional<Hospedes> findByEmail(String email);

	boolean existsByEmail(String email);

	boolean existsByCpf(String cpf);
}
