package com.grp.trackDesk.exception;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiError {

	 private HttpStatus status;
	    private int code;
	    
	    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
	    private LocalDateTime timestamp;
	    
	    private String message;
	    private String debugMessage;
	    private String path;
	    private List<ApiSubError> subErrors;
	    
	    private ApiError() {
	        timestamp = LocalDateTime.now();
	    }
	    
	    public ApiError(HttpStatus status) {
	        this();
	        this.status = status;
	        this.code = status.value();
	    }
	    
	    public ApiError(HttpStatus status, String message) {
	        this();
	        this.status = status;
	        this.code = status.value();
	        this.message = message;
	    }
	    
	    public ApiError(HttpStatus status, String message, String debugMessage) {
	        this();
	        this.status = status;
	        this.code = status.value();
	        this.message = message;
	        this.debugMessage = debugMessage;
	    }
	 // Sous-classes pour les erreurs de validation
	    @Data
	    public static class ApiSubError {
	        private String field;
	        private Object rejectedValue;
	        private String message;
	        
	        public ApiSubError(String field, Object rejectedValue, String message) {
	            this.field = field;
	            this.rejectedValue = rejectedValue;
	            this.message = message;
	        }
	    }
}
