package br.encibra.desafio.infra.controllers;

import br.encibra.desafio.domain.entities.Password;
import br.encibra.desafio.domain.services.PasswordService;
import br.encibra.desafio.infra.mapper.PasswordHttpMapper;
import br.encibra.desafio.infra.request.PasswordHttpRequest;
import br.encibra.desafio.infra.response.PasswordHttpResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<PasswordHttpResponse> create (@RequestBody PasswordHttpRequest request) {
        Password password = passwordService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(password));
    }

}
