package com.nebulahotel.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "reservas")
public class Reservas {

    @Id
    @Column(name = "id_reserva")
    @JsonProperty("_id")
    private Integer idReserva;

    @Column(name = "id_hospede", nullable = false)
    @JsonProperty("hospede_id")
    private Integer idHospede;

    @Column(name = "id_quarto", nullable = false)
    @JsonProperty("quarto_id")
    private Integer idQuarto;

    @Column(name = "data_reserva", nullable = false)
    private LocalDateTime dataReserva;

    @Column(name = "data_checkin", nullable = false)
    @JsonProperty("data_checkin")
    private LocalDate dataCheckin;

    @Column(name = "data_checkout", nullable = false)
    @JsonProperty("data_checkout")
    private LocalDate dataCheckout;

    @Column(name = "status", nullable = false, length = 20)
    private String status;

    @Column(name = "canal", nullable = false, length = 30)
    private String canal;

    @Column(name = "valor_previsto", nullable = false)
    @JsonProperty("valor_previsto")
    private BigDecimal valorPrevisto;

    public Integer getIdReserva() {
        return idReserva;
    }

    public void setIdReserva(Integer idReserva) {
        this.idReserva = idReserva;
    }

    public Integer getIdHospede() {
        return idHospede;
    }

    public void setIdHospede(Integer idHospede) {
        this.idHospede = idHospede;
    }

    public Integer getIdQuarto() {
        return idQuarto;
    }

    public void setIdQuarto(Integer idQuarto) {
        this.idQuarto = idQuarto;
    }

    public LocalDateTime getDataReserva() {
        return dataReserva;
    }

    public void setDataReserva(LocalDateTime dataReserva) {
        this.dataReserva = dataReserva;
    }

    public LocalDate getDataCheckin() {
        return dataCheckin;
    }

    public void setDataCheckin(LocalDate dataCheckin) {
        this.dataCheckin = dataCheckin;
    }

    public LocalDate getDataCheckout() {
        return dataCheckout;
    }

    public void setDataCheckout(LocalDate dataCheckout) {
        this.dataCheckout = dataCheckout;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCanal() {
        return canal;
    }

    public void setCanal(String canal) {
        this.canal = canal;
    }

    public BigDecimal getValorPrevisto() {
        return valorPrevisto;
    }

    public void setValorPrevisto(BigDecimal valorPrevisto) {
        this.valorPrevisto = valorPrevisto;
    }
}
