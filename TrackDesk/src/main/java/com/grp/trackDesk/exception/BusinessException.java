package com.grp.trackDesk.exception;
import org.springframework.http.HttpStatus;
import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException{
	private final HttpStatus status;
    private final String debugMessage;
    
    public BusinessException(String message) {
        super(message);
        this.status = HttpStatus.BAD_REQUEST;
        this.debugMessage = null;
    }
    
    public BusinessException(String message, HttpStatus status) {
        super(message);
        this.status = status;
        this.debugMessage = null;
    }
    
    public BusinessException(String message, String debugMessage, HttpStatus status) {
        super(message);
        this.status = status;
        this.debugMessage = debugMessage;
    }
}



