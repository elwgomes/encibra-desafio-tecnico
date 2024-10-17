package br.encibra.desafio.exceptions;

public class PasswordDecryptionException extends RuntimeException {
	public PasswordDecryptionException(String message) {
		super(message);
	}

	public PasswordDecryptionException(String message, Throwable cause) {
		super(message, cause);
	}
}
