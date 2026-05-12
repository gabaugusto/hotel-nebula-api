package com.nebulahotel.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "pagamentos")
public class Pagamentos {

    @Id
    @Column(name = "id_pagamento")
    @JsonProperty("_id")
    private Integer idPagamento;

    @Column(name = "id_hospedagem", nullable = false)
    @JsonProperty("hospedagem_id")
    private Integer idHospedagem;

    @Column(name = "valor", nullable = false)
    @JsonProperty("valor_pago")
    private BigDecimal valor;

    @Column(name = "metodo", nullable = false, length = 30)
    @JsonProperty("metodo_pagamento")
    private String metodo;

    @Column(name = "status", nullable = false, length = 20)
    private String status;

    @Column(name = "data_pagamento")
    @JsonProperty("data_pagamento")
    private LocalDateTime dataPagamento;

    @Column(name = "transacao_id", length = 60)
    @JsonProperty("transacao_id")
    private String transacaoId;

    public Integer getIdPagamento() {
        return idPagamento;
    }

    public void setIdPagamento(Integer idPagamento) {
        this.idPagamento = idPagamento;
    }

    public Integer getIdHospedagem() {
        return idHospedagem;
    }

    public void setIdHospedagem(Integer idHospedagem) {
        this.idHospedagem = idHospedagem;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public String getMetodo() {
        return metodo;
    }

    public void setMetodo(String metodo) {
        this.metodo = metodo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getDataPagamento() {
        return dataPagamento;
    }

    public void setDataPagamento(LocalDateTime dataPagamento) {
        this.dataPagamento = dataPagamento;
    }

    public String getTransacaoId() {
        return transacaoId;
    }

    public void setTransacaoId(String transacaoId) {
        this.transacaoId = transacaoId;
    }
}
