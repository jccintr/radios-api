package com.jcsoftware.radios.services.exceptions;

public class ForbiddenException extends RuntimeException {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public ForbiddenException(Object id) {
		super("Access denied for Id: " + id);
	}
}
