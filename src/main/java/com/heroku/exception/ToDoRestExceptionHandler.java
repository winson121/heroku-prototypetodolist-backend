package com.heroku.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ToDoRestExceptionHandler {
	
	// Add an exception handler for ToDoNotFoundException
	
	@ExceptionHandler
	public ResponseEntity<ToDoErrorResponse> handleException(ToDoNotFoundException exc) {
		
		// create ToDoErrorResponse
		ToDoErrorResponse error = new ToDoErrorResponse(
									HttpStatus.NOT_FOUND.value(),
									exc.getMessage(),
									System.currentTimeMillis()
									);
		
		// return ResponseEntity
		return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
	}
	
	// Add another exception handler to catch any exception
	
	@ExceptionHandler
	public ResponseEntity<ToDoErrorResponse> handleException(Exception exc) {
		
		// create ToDoErrorResponse
		ToDoErrorResponse error = new ToDoErrorResponse(
									HttpStatus.BAD_REQUEST.value(),
									exc.getMessage(),
									System.currentTimeMillis());
		// return ResponseEntity
		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}
}
