package com.jcsoftware.radios.services.exceptions;

public class RadioAlreadyInListException extends RuntimeException {
	
private static final long serialVersionUID = 1L;
	
	public RadioAlreadyInListException(String msg) {
		super(msg);
	}

}
