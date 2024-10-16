package br.encibra.desafio.infra.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.encibra.desafio.domain.entities.Password;
import br.encibra.desafio.domain.services.PasswordService;
import br.encibra.desafio.infra.mapper.PasswordHttpMapper;
import br.encibra.desafio.infra.request.PasswordHttpRequest;
import br.encibra.desafio.infra.response.PasswordHttpResponse;

@RestController
@RequestMapping("passwords")
public class PasswordController {

	private final PasswordService passwordService;
	private final PasswordHttpMapper mapper;

	public PasswordController(PasswordService passwordService, PasswordHttpMapper mapper) {
		this.passwordService = passwordService;
		this.mapper = mapper;
	}

	@PostMapping
	public ResponseEntity<PasswordHttpResponse> create(@RequestBody PasswordHttpRequest request) throws Exception {
		Password password = passwordService.addPasswordToUser(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(password));
	}

	@PatchMapping("{passwordId}")
	public ResponseEntity<PasswordHttpResponse> update(@PathVariable Long passwordId,
			@RequestBody PasswordHttpRequest request) throws Exception {
		Password password = passwordService.update(passwordId, request);
		return ResponseEntity.status(HttpStatus.OK).body(mapper.toResponse(password));
	}

	@DeleteMapping("{passwordId}")
	public ResponseEntity<Void> delete(@PathVariable Long passwordId) {
		passwordService.delete(passwordId);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

}
