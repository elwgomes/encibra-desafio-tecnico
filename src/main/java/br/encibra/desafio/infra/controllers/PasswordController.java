package br.encibra.desafio.infra.controllers;

import br.encibra.desafio.domain.services.TokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.encibra.desafio.domain.entities.Password;
import br.encibra.desafio.domain.services.PasswordService;
import br.encibra.desafio.infra.mapper.PasswordHttpMapper;
import br.encibra.desafio.infra.request.PasswordHttpRequest;
import br.encibra.desafio.infra.response.PasswordHttpResponse;

import java.util.List;

@RestController
@RequestMapping("passwords")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class PasswordController {

	private final PasswordService passwordService;
	private final TokenService tokenService;
	private final PasswordHttpMapper mapper;

	@Operation(summary = "Retrieve all passwords",
			description = "This endpoint retrieves all passwords associated with the authenticated user.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Passwords retrieved successfully",
					content = @Content(schema = @Schema(implementation = PasswordHttpResponse.class))),
			@ApiResponse(responseCode = "401", description = "Unauthorized access"),
			@ApiResponse(responseCode = "500", description = "Internal server error")
	})
	@GetMapping
	public ResponseEntity<List<PasswordHttpResponse>> findAll(
			@Parameter(description = "Bearer token for authentication", required = true)
			@RequestHeader("Authorization") String authorizationHeader
	) throws Exception {
		String token = authorizationHeader.replace("Bearer ", "");
		List<PasswordHttpResponse> list = mapper.toListResponse(passwordService.findAll(token));
		return ResponseEntity.status(HttpStatus.OK).body(list);
	}

	@Operation(summary = "Create a new password",
			description = "This endpoint creates a new password for the authenticated user.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Password created successfully",
					content = @Content(schema = @Schema(implementation = PasswordHttpResponse.class))),
			@ApiResponse(responseCode = "400", description = "Invalid input provided"),
			@ApiResponse(responseCode = "401", description = "Unauthorized access"),
			@ApiResponse(responseCode = "500", description = "Internal server error")
	})
	@PostMapping("create")
	public ResponseEntity<PasswordHttpResponse> create(
			@Parameter(description = "Bearer token for authentication", required = true)
			@RequestHeader("Authorization") String authorizationHeader,
			@Parameter(description = "Details of the password to create", required = true)
			@RequestBody PasswordHttpRequest request) throws Exception {
		String token = authorizationHeader.replace("Bearer ", "");
		Password password = passwordService.addPasswordToUser(request, token);
		return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(password));
	}

	@Operation(summary = "Update an existing password",
			description = "This endpoint updates an existing password for the authenticated user.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Password updated successfully",
					content = @Content(schema = @Schema(implementation = PasswordHttpResponse.class))),
			@ApiResponse(responseCode = "404", description = "Password not found"),
			@ApiResponse(responseCode = "401", description = "Unauthorized access"),
			@ApiResponse(responseCode = "500", description = "Internal server error")
	})
	@PatchMapping("update/{passwordId}")
	public ResponseEntity<PasswordHttpResponse> update(
			@Parameter(description = "ID of the password to update", required = true)
			@PathVariable Long passwordId,
			@Parameter(description = "Bearer token for authentication", required = true)
			@RequestHeader("Authorization") String authorizationHeader,
			@Parameter(description = "Details of the password to update", required = true)
			@RequestBody PasswordHttpRequest request) throws Exception {
		String token = authorizationHeader.replace("Bearer ", "");
		Password password = passwordService.update(passwordId, request, token);
		return ResponseEntity.status(HttpStatus.OK).body(mapper.toResponse(password));
	}

	@Operation(summary = "Delete a password",
			description = "This endpoint deletes an existing password for the authenticated user.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "204", description = "Password deleted successfully"),
			@ApiResponse(responseCode = "404", description = "Password not found"),
			@ApiResponse(responseCode = "401", description = "Unauthorized access"),
			@ApiResponse(responseCode = "500", description = "Internal server error")
	})
	@DeleteMapping("delete/{passwordId}")
	public ResponseEntity<Void> delete(
			@Parameter(description = "Bearer token for authentication", required = true)
			@RequestHeader("Authorization") String authorizationHeader,
			@Parameter(description = "ID of the password to delete", required = true)
			@PathVariable Long passwordId) throws Exception {
		String token = authorizationHeader.replace("Bearer ", "");
		passwordService.delete(passwordId, token);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

}