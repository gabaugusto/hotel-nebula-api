package com.nebulahotel.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "feedbacks")
public class Avaliacoes {

    @Id
    @Column(name = "id_feedback")
    private Integer idFeedback;

    @Column(name = "id_hospede", nullable = false)
    private Integer idHospede;

    @Column(name = "id_quarto", nullable = false)
    private Integer idQuarto;

    @Column(name = "id_hospedagem", nullable = false)
    private Integer idHospedagem;

    @Column(name = "nota_geral", nullable = false)
    private BigDecimal notaGeral;

    @Lob
    @Column(name = "comentario")
    private String comentario;

    @Column(name = "data_avaliacao", nullable = false)
    private LocalDateTime dataAvaliacao;

    public Integer getIdFeedback() {
        return idFeedback;
    }

    public void setIdFeedback(Integer idFeedback) {
        this.idFeedback = idFeedback;
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

    public Integer getIdHospedagem() {
        return idHospedagem;
    }

    public void setIdHospedagem(Integer idHospedagem) {
        this.idHospedagem = idHospedagem;
    }

    public BigDecimal getNotaGeral() {
        return notaGeral;
    }

    public void setNotaGeral(BigDecimal notaGeral) {
        this.notaGeral = notaGeral;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public LocalDateTime getDataAvaliacao() {
        return dataAvaliacao;
    }

    public void setDataAvaliacao(LocalDateTime dataAvaliacao) {
        this.dataAvaliacao = dataAvaliacao;
    }
}
