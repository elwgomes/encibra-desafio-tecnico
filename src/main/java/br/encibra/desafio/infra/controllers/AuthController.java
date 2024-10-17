package br.encibra.desafio.infra.controllers;

import br.encibra.desafio.domain.entities.User;
import br.encibra.desafio.domain.services.TokenService;
import br.encibra.desafio.domain.services.UserService;
import br.encibra.desafio.infra.response.AccessHttpResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
public class AuthController {

    private final TokenService tokenService;
    private final UserService userService;

    @Operation(summary = "Authenticate user",
            description = "This endpoint authenticates a user by their user ID and generates a JWT token.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Authentication successful",
                    content = @Content(schema = @Schema(implementation = AccessHttpResponse.class))),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("{userId}")
    public ResponseEntity<AccessHttpResponse> authenticate(
            @Parameter(description = "ID of the user to authenticate", required = true)
            @PathVariable Long userId) {
        AccessHttpResponse response = new AccessHttpResponse(tokenService.generateJwtToken(userId));
        User user = userService.findById(userId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}