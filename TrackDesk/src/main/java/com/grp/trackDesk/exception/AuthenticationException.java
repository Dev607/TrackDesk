package com.grp.trackDesk.exception;

import org.springframework.http.HttpStatus;

public class AuthenticationException extends RuntimeException{
	
	private final HttpStatus status;
	
	 public AuthenticationException(String message) {
	        super(message);
	        this.status = HttpStatus.UNAUTHORIZED;
	    }

	    public HttpStatus getStatus() {
	        return status;
	    }
	

}
