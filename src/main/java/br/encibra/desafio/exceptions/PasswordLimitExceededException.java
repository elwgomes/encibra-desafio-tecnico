package br.encibra.desafio.exceptions;

public class PasswordLimitExceededException extends RuntimeException {
	public PasswordLimitExceededException(String message) {
		super(message);
	}
}
