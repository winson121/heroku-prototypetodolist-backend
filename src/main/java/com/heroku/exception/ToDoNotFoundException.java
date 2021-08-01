package com.heroku.exception;

public class ToDoNotFoundException extends RuntimeException {

	public ToDoNotFoundException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public ToDoNotFoundException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public ToDoNotFoundException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}
	
}
