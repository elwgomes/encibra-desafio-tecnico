package br.encibra.desafio.domain.services;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class TokenService {

	@Value("${jwt.secret}")
	private String secret;

	public String generateJwtToken(Long userId) {
		try {
			Algorithm algorithm = Algorithm.HMAC256(secret);
			return JWT.create()
					.withIssuer("authenticate-encibra-api")
					.withSubject(String.valueOf(userId))
					.withExpiresAt(this.generateExpiration())
					.sign(algorithm);
		} catch (JWTCreationException e) {
			throw new RuntimeException("Error while generating token.");
		}
	}

	public String validateJwtToken(String token) {
		try {
			Algorithm algorithm = Algorithm.HMAC256(secret);
			return JWT.require(algorithm)
					.withIssuer("authenticate-encibra-api")
					.build()
					.verify(token)
					.getSubject();
		} catch (JWTVerificationException e) {
			throw new RuntimeException("Error while validating token.");
		}
	}

	public String extractTokenFromRequest(HttpServletRequest request) {
		String bearerToken = request.getHeader("Authorization");
		if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring(7);
		}
		return null;
	}

	public Instant generateExpiration() {
		return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.ofHours(-3));
	}

}
