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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("users")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;
	private final UserHttpMapper mapper;

	@Operation(summary = "Create a new user", description = "This endpoint creates a new user and returns the created user details.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "User created successfully", content = @Content(schema = @Schema(implementation = UserHttpResponse.class))),
			@ApiResponse(responseCode = "400", description = "Invalid input provided"),
			@ApiResponse(responseCode = "500", description = "Internal server error")
	})
	@PostMapping("create")
	public ResponseEntity<UserHttpResponse> create(
			@Parameter(description = "User details for creating a new user", required = true) @RequestBody UserHttpRequest request) {
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(mapper.toResponse(userService.save(mapper.toDomain(request))));
	}

	@SecurityRequirement(name = "bearerAuth")
	@Operation(summary = "Get user by ID", description = "This endpoint retrieves a user by their ID, with decrypted details.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "User retrieved successfully", content = @Content(schema = @Schema(implementation = UserHttpResponseWithList.class))),
			@ApiResponse(responseCode = "404", description = "User not found"),
			@ApiResponse(responseCode = "500", description = "Internal server error")
	})
	@GetMapping("{id}")
	public ResponseEntity<UserHttpResponseWithList> getAll(
			@Parameter(description = "ID of the user to retrieve", required = true) @PathVariable Long id)
			throws Exception {
		return ResponseEntity.status(HttpStatus.OK)
				.body(mapper.toListResponse(userService.findByIdWithDecrypt(id)));
	}

}
