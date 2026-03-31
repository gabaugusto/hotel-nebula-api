package com.nebulahotel.repository;

import com.nebulahotel.model.Reservas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReservasRepository extends JpaRepository<Reservas, Integer> {

    @Query("SELECT r FROM Reservas r WHERE LOWER(r.status) IN ('confirmada', 'ativa', 'em_andamento')")
    List<Reservas> findReservasAtivas();

    List<Reservas> findByIdHospedeOrderByDataReservaDesc(Integer idHospede);
}
