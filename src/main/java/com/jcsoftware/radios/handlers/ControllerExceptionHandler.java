package com.jcsoftware.radios.handlers;

import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.jcsoftware.radios.controllers.exceptions.StandardError;
import com.jcsoftware.radios.services.exceptions.ForbiddenException;
import com.jcsoftware.radios.services.exceptions.RadioAlreadyInListException;
import com.jcsoftware.radios.services.exceptions.ResourceNotFoundException;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ControllerExceptionHandler {
	
	
	
	
	
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<StandardError> resourceNotFound(ResourceNotFoundException e,HttpServletRequest request){
		
		String error = "Resource not found";
		HttpStatus status = HttpStatus.NOT_FOUND;
		StandardError err = new StandardError(Instant.now(),status.value(),error,e.getMessage(),request.getRequestURI());
		return ResponseEntity.status(status).body(err);
	}
	
	@ExceptionHandler(RadioAlreadyInListException.class)
	public ResponseEntity<StandardError> radioAlreadyInList(RadioAlreadyInListException e,HttpServletRequest request){
		
		String error = "Conflict";
		HttpStatus status = HttpStatus.CONFLICT;
		StandardError err = new StandardError(Instant.now(),status.value(),error,e.getMessage(),request.getRequestURI());
		return ResponseEntity.status(status).body(err);
	}
	
	@ExceptionHandler(ForbiddenException.class)
	public ResponseEntity<StandardError> forbiden(ForbiddenException e, HttpServletRequest request) {

		String error = "Access denied";
		HttpStatus status = HttpStatus.FORBIDDEN;
		StandardError err = new StandardError(Instant.now(), status.value(), error, e.getMessage(),
				request.getRequestURI());
		return ResponseEntity.status(status).body(err);
	}
	
	
	
	
}
