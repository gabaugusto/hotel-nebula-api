package com.nebulahotel.controller;

import com.nebulahotel.model.Hospedes;
import com.nebulahotel.repository.HospedesRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/hospedes")
public class HospedesController {

	private final HospedesRepository hospedesRepository;

	public HospedesController(HospedesRepository hospedesRepository) {
		this.hospedesRepository = hospedesRepository;
	}

	@GetMapping
	public List<Hospedes> listarTodos() {
		return hospedesRepository.findAll();
	}

	@GetMapping("/{id}")
	public ResponseEntity<Hospedes> buscarPorId(@PathVariable Integer id) {
		return hospedesRepository.findById(id)
				.map(ResponseEntity::ok)
				.orElseGet(() -> ResponseEntity.notFound().build());
	}

	@GetMapping("/email/{email}")
	public ResponseEntity<Hospedes> buscarPorEmail(@PathVariable String email) {
		return hospedesRepository.findByEmail(email)
				.map(ResponseEntity::ok)
				.orElseGet(() -> ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<?> criar(@RequestBody Hospedes hospede) {
		if (hospede.getIdHospede() == null) {
			return ResponseEntity.badRequest().body(Map.of("erro", "O campo idHospede é obrigatório."));
		}

		if (hospedesRepository.existsById(hospede.getIdHospede())) {
			return ResponseEntity.status(HttpStatus.CONFLICT)
					.body(Map.of("erro", "Já existe hóspede com este idHospede."));
		}

		if (hospede.getEmail() != null && hospedesRepository.existsByEmail(hospede.getEmail())) {
			return ResponseEntity.status(HttpStatus.CONFLICT)
					.body(Map.of("erro", "Já existe hóspede com este email."));
		}

		if (hospede.getCpf() != null && hospedesRepository.existsByCpf(hospede.getCpf())) {
			return ResponseEntity.status(HttpStatus.CONFLICT)
					.body(Map.of("erro", "Já existe hóspede com este CPF."));
		}

		if (hospede.getDataCadastro() == null) {
			hospede.setDataCadastro(LocalDateTime.now());
		}

		if (hospede.getAtivo() == null) {
			hospede.setAtivo(true);
		}

		Hospedes salvo = hospedesRepository.save(hospede);
		return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> atualizar(@PathVariable Integer id, @RequestBody Hospedes hospedeAtualizado) {
		Optional<Hospedes> hospedeExistenteOpt = hospedesRepository.findById(id);
		if (hospedeExistenteOpt.isEmpty()) {
			return ResponseEntity.notFound().build();
		}

		Hospedes hospedeExistente = hospedeExistenteOpt.get();

		if (hospedeAtualizado.getEmail() != null && !hospedeAtualizado.getEmail().equals(hospedeExistente.getEmail())) {
			if (hospedesRepository.existsByEmail(hospedeAtualizado.getEmail())) {
				return ResponseEntity.status(HttpStatus.CONFLICT)
						.body(Map.of("erro", "Já existe hóspede com este email."));
			}
		}

		if (hospedeAtualizado.getCpf() != null && !hospedeAtualizado.getCpf().equals(hospedeExistente.getCpf())) {
			if (hospedesRepository.existsByCpf(hospedeAtualizado.getCpf())) {
				return ResponseEntity.status(HttpStatus.CONFLICT)
						.body(Map.of("erro", "Já existe hóspede com este CPF."));
			}
		}

		hospedeAtualizado.setIdHospede(id);
		if (hospedeAtualizado.getDataCadastro() == null) {
			hospedeAtualizado.setDataCadastro(hospedeExistente.getDataCadastro());
		}

		if (hospedeAtualizado.getAtivo() == null) {
			hospedeAtualizado.setAtivo(hospedeExistente.getAtivo());
		}

		Hospedes salvo = hospedesRepository.save(hospedeAtualizado);
		return ResponseEntity.ok(salvo);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> remover(@PathVariable Integer id) {
		if (!hospedesRepository.existsById(id)) {
			return ResponseEntity.notFound().build();
		}

		hospedesRepository.deleteById(id);
		return ResponseEntity.noContent().build();
	}
}
