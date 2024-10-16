package br.encibra.desafio.infra.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.encibra.desafio.domain.services.UserService;
import br.encibra.desafio.infra.mapper.UserHttpMapper;
import br.encibra.desafio.infra.request.UserHttpRequest;
import br.encibra.desafio.infra.response.UserHttpResponse;
import br.encibra.desafio.infra.response.UserHttpResponseWithList;

@RestController
@RequestMapping("users")
public class UserController {

	private final UserService userService;
	private final UserHttpMapper mapper;

	public UserController(UserService userService, UserHttpMapper mapper) {
		this.userService = userService;
		this.mapper = mapper;
	}

	@PostMapping
	public ResponseEntity<UserHttpResponse> create(@RequestBody UserHttpRequest request) {
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(mapper.toResponse(userService.save(mapper.toDomain(request))));
	}

	@GetMapping("{id}")
	public ResponseEntity<UserHttpResponseWithList> read(@PathVariable Long id) throws Exception {
		return ResponseEntity.status(HttpStatus.OK).body(mapper.toListResponse(userService.findById(id)));
	}

}
