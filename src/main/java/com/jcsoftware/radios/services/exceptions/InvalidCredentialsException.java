package com.jcsoftware.radios.services.exceptions;

public class InvalidCredentialsException  extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	
	public InvalidCredentialsException(String msg) {
		super(msg);
	}

}
