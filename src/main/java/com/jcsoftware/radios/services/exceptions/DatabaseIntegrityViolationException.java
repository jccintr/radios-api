package com.jcsoftware.radios.services.exceptions;

public class DatabaseIntegrityViolationException extends RuntimeException {
	
private static final long serialVersionUID = 1L;
	
	public DatabaseIntegrityViolationException(Object id) {
		super("Referential integrity constraint violation for Id: " + id);
	}
}
