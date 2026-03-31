package com.nebulahotel.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "hospedes")
public class Hospedes {

	@Id
	@Column(name = "id_hospede")
	private Integer idHospede;

	@Column(name = "nome", nullable = false, length = 100)
	private String nome;

	@Column(name = "email", nullable = false, unique = true, length = 120)
	private String email;

	@Column(name = "cpf", nullable = false, unique = true, length = 14)
	private String cpf;

	@Column(name = "telefone", length = 20)
	private String telefone;

	@Column(name = "data_nascimento")
	private LocalDate dataNascimento;

	@Column(name = "data_cadastro", nullable = false)
	private LocalDateTime dataCadastro;

	@Column(name = "ativo", nullable = false)
	private Boolean ativo;

	public Integer getIdHospede() {
		return idHospede;
	}

	public void setIdHospede(Integer idHospede) {
		this.idHospede = idHospede;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public LocalDate getDataNascimento() {
		return dataNascimento;
	}

	public void setDataNascimento(LocalDate dataNascimento) {
		this.dataNascimento = dataNascimento;
	}

	public LocalDateTime getDataCadastro() {
		return dataCadastro;
	}

	public void setDataCadastro(LocalDateTime dataCadastro) {
		this.dataCadastro = dataCadastro;
	}

	public Boolean getAtivo() {
		return ativo;
	}

	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}
}
