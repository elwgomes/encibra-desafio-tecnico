package br.encibra.desafio.infra.controllers.exceptions.handling;

import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import br.encibra.desafio.exceptions.PasswordLimitExceededException;
import br.encibra.desafio.exceptions.ResourceNotFoundException;
import br.encibra.desafio.infra.controllers.exceptions.response.ErrorDetail;
import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class ControllerExceptionHandler {

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ErrorDetail> resourceNotFound(ResourceNotFoundException e, HttpServletRequest request) {
		String error = "RESOURCE NOT FOUND";
		HttpStatus status = HttpStatus.NOT_FOUND;
		ErrorDetail err = new ErrorDetail(Instant.now(), status.value(), error, e.getMessage(),
				request.getRequestURI());
		return ResponseEntity.status(status).body(err);
	}

	@ExceptionHandler(PasswordLimitExceededException.class)
	public ResponseEntity<ErrorDetail> passwordLimitExceeded(PasswordLimitExceededException e,
			HttpServletRequest request) {
		String error = "PASSWORD LIMIT EXCEEDED";
		HttpStatus status = HttpStatus.BAD_REQUEST;
		ErrorDetail err = new ErrorDetail(Instant.now(), status.value(), error, e.getMessage(),
				request.getRequestURI());
		return ResponseEntity.status(status).body(err);
	}

}
