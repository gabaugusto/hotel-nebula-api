package com.nebulahotel.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "hospedagens")
public class Hospedagens {

    @Id
    @Column(name = "id_hospedagem")
    @JsonProperty("_id")
    private Integer idHospedagem;

    @Column(name = "id_reserva", nullable = false)
    @JsonProperty("reserva_id")
    private Integer idReserva;

    @Column(name = "id_funcionario_checkin")
    private Integer idFuncionarioCheckin;

    @Column(name = "id_funcionario_checkout")
    private Integer idFuncionarioCheckout;

    @Column(name = "checkin_real")
    @JsonProperty("checkin_real")
    private LocalDateTime checkinReal;

    @Column(name = "checkout_real")
    @JsonProperty("checkout_real")
    private LocalDateTime checkoutReal;

    @Column(name = "status", nullable = false, length = 20)
    private String status;

    @Column(name = "total_diarias", nullable = false)
    @JsonProperty("total_diarias")
    private BigDecimal totalDiarias;

    @Column(name = "total_servicos", nullable = false)
    @JsonProperty("total_servicos")
    private BigDecimal totalServicos;

    @Column(name = "total_geral", nullable = false)
    @JsonProperty("total_geral")
    private BigDecimal totalGeral;

    public Integer getIdHospedagem() {
        return idHospedagem;
    }

    public void setIdHospedagem(Integer idHospedagem) {
        this.idHospedagem = idHospedagem;
    }

    public Integer getIdReserva() {
        return idReserva;
    }

    public void setIdReserva(Integer idReserva) {
        this.idReserva = idReserva;
    }

    public Integer getIdFuncionarioCheckin() {
        return idFuncionarioCheckin;
    }

    public void setIdFuncionarioCheckin(Integer idFuncionarioCheckin) {
        this.idFuncionarioCheckin = idFuncionarioCheckin;
    }

    public Integer getIdFuncionarioCheckout() {
        return idFuncionarioCheckout;
    }

    public void setIdFuncionarioCheckout(Integer idFuncionarioCheckout) {
        this.idFuncionarioCheckout = idFuncionarioCheckout;
    }

    public LocalDateTime getCheckinReal() {
        return checkinReal;
    }

    public void setCheckinReal(LocalDateTime checkinReal) {
        this.checkinReal = checkinReal;
    }

    public LocalDateTime getCheckoutReal() {
        return checkoutReal;
    }

    public void setCheckoutReal(LocalDateTime checkoutReal) {
        this.checkoutReal = checkoutReal;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getTotalDiarias() {
        return totalDiarias;
    }

    public void setTotalDiarias(BigDecimal totalDiarias) {
        this.totalDiarias = totalDiarias;
    }

    public BigDecimal getTotalServicos() {
        return totalServicos;
    }

    public void setTotalServicos(BigDecimal totalServicos) {
        this.totalServicos = totalServicos;
    }

    public BigDecimal getTotalGeral() {
        return totalGeral;
    }

    public void setTotalGeral(BigDecimal totalGeral) {
        this.totalGeral = totalGeral;
    }
}
