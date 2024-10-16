package br.encibra.desafio.infra.controllers;

import br.encibra.desafio.domain.entities.Password;
import br.encibra.desafio.domain.services.PasswordService;
import br.encibra.desafio.infra.mapper.PasswordHttpMapper;
import br.encibra.desafio.infra.request.PasswordHttpRequest;
import br.encibra.desafio.infra.response.PasswordHttpResponse;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


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
    public ResponseEntity<PasswordHttpResponse> create (@RequestBody PasswordHttpRequest request) throws Exception {
        Password password = passwordService.addPasswordToUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(password));
    }

    @PatchMapping("{id}")
    public ResponseEntity<PasswordHttpResponse> update (@PathVariable Long id, @RequestBody PasswordHttpRequest request) throws Exception {
        Password password = passwordService.update(id, request);
        return ResponseEntity.status(HttpStatus.OK).body(mapper.toResponse(password));
    }

}
