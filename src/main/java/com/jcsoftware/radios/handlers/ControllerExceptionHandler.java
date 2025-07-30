package com.jcsoftware.radios.handlers;

import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.jcsoftware.radios.controllers.exceptions.StandardError;
import com.jcsoftware.radios.controllers.exceptions.ValidationError;
import com.jcsoftware.radios.services.exceptions.DatabaseIntegrityViolationException;
import com.jcsoftware.radios.services.exceptions.DuplicatedEmailException;
import com.jcsoftware.radios.services.exceptions.ForbiddenException;
import com.jcsoftware.radios.services.exceptions.InvalidCredentialsException;
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
	
	@ExceptionHandler(DuplicatedEmailException.class)
	public ResponseEntity<StandardError> duplicatedEmail(DuplicatedEmailException e,HttpServletRequest request){
		
		String error = "Conflict";
		HttpStatus status = HttpStatus.CONFLICT;
		StandardError err = new StandardError(Instant.now(),status.value(),error,e.getMessage(),request.getRequestURI());
		return ResponseEntity.status(status).body(err);
		
	}
	

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<StandardError> methodArgumentNotValid(MethodArgumentNotValidException e,HttpServletRequest request){
		String error = "Invalid Argument";
		HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;
		ValidationError err = new ValidationError(Instant.now(),status.value(),error,"Invalid parameters",request.getRequestURI());
		for(FieldError f : e.getBindingResult().getFieldErrors()) {
			err.AddError(f.getField(), f.getDefaultMessage());
		}
		return ResponseEntity.status(status).body(err);
	}
	
	@ExceptionHandler(InvalidFormatException.class)
	public ResponseEntity<StandardError> handleInvalidFormat(InvalidFormatException e,HttpServletRequest request){
		String error = "Invalid Argument";
		HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;
		ValidationError err = new ValidationError(Instant.now(),status.value(),error,"Invalid parameters",request.getRequestURI());
		//for(FieldError f : e.getBindingResult().getFieldErrors()) {
			err.AddError("state", "Estado inexistente");
		//}
		return ResponseEntity.status(status).body(err);
	}
	
	@ExceptionHandler(InvalidCredentialsException.class)
	public ResponseEntity<StandardError> invalidCredentials(InvalidCredentialsException e,HttpServletRequest request){
		
		String error = "Bad Request";
		HttpStatus status = HttpStatus.BAD_REQUEST;
		StandardError err = new StandardError(Instant.now(),status.value(),error,e.getMessage(),request.getRequestURI());
		return ResponseEntity.status(status).body(err);
		
	}
	@ExceptionHandler(DatabaseIntegrityViolationException.class)
	public ResponseEntity<StandardError> DatabaseIntegrityViolation(DatabaseIntegrityViolationException e, HttpServletRequest request) {

		String error = "Database Integrity Violation";
		HttpStatus status = HttpStatus.CONFLICT;
		StandardError err = new StandardError(Instant.now(), status.value(), error, e.getMessage(),
				request.getRequestURI());
		return ResponseEntity.status(status).body(err);
	}
	
	
	
}
