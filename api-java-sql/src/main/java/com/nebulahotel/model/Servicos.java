package com.nebulahotel.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;

@Entity
@Table(name = "servicos")
public class Servicos {

    @Id
    @Column(name = "id_servico")
    private Integer idServico;

    @Column(name = "nome", nullable = false, length = 80)
    private String nome;

    @Column(name = "categoria", nullable = false, length = 40)
    private String categoria;

    @Column(name = "preco", nullable = false)
    private BigDecimal preco;

    @Column(name = "disponivel", nullable = false)
    private Boolean disponivel;

    public Integer getIdServico() {
        return idServico;
    }

    public void setIdServico(Integer idServico) {
        this.idServico = idServico;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }

    public Boolean getDisponivel() {
        return disponivel;
    }

    public void setDisponivel(Boolean disponivel) {
        this.disponivel = disponivel;
    }
}
