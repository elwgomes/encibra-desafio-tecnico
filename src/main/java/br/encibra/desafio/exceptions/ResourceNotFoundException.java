package br.encibra.desafio.exceptions;

public class ResourceNotFoundException extends RuntimeException {
	private final Long resourceId;

	public ResourceNotFoundException(Long resourceId) {
		super("Resource not found with ID: " + resourceId);
		this.resourceId = resourceId;
	}

	public Long getResourceId() {
		return resourceId;
	}
}
