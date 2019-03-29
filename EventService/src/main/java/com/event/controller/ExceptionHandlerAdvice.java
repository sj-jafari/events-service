package com.event.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Exceptions thrown from controller layer are handled through this class. Each
 * Exception-specific method, generally creates a json response indicating error
 * detail and returns it with the proper error code.
 * 
 * @author Jalal
 * @since 20190324
 * @version 1.0
 */
@ControllerAdvice
public class ExceptionHandlerAdvice {

	@ExceptionHandler(Exception.class)
	public ResponseEntity<?> handleException(Exception e) {
		// log exception
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
	}
}