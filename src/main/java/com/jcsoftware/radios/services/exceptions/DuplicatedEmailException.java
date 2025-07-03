package com.jcsoftware.radios.services.exceptions;

public class DuplicatedEmailException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public DuplicatedEmailException() {
		super("Email já cadastrado");
	}

}
