package com.grp.trackDesk.exception;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;


import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;


import java.util.stream.Collectors;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.authentication.BadCredentialsException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
	// Validation errors (DTO)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ApiError> handleValidationExceptions(MethodArgumentNotValidException ex, WebRequest request) {

		ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, "Erreur de validation");

		// Extraire les erreurs de validation
		apiError.setSubErrors(ex.getBindingResult().getAllErrors().stream().map(error -> {
			String fieldName = ((FieldError) error).getField();
			String errorMessage = error.getDefaultMessage();
			Object rejectedValue = ((FieldError) error).getRejectedValue();
			return new ApiError.ApiSubError(fieldName, rejectedValue, errorMessage);
		}).collect(Collectors.toList()));

		apiError.setPath(((ServletWebRequest) request).getRequest().getRequestURI());
		log.warn("Validation error: {}", apiError.getMessage());

		return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
	}

	// Entity not found
	@ExceptionHandler(EntityNotFoundException.class)
	public ResponseEntity<ApiError> handleEntityNotFound(EntityNotFoundException ex, WebRequest request) {
		ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, "Ressource non trouvée", ex.getMessage());
		apiError.setPath(((ServletWebRequest) request).getRequest().getRequestURI());
		log.warn("Entity not found: {}", ex.getMessage());
		return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
	}

	// Custom business exceptions
	@ExceptionHandler(BusinessException.class)
	public ResponseEntity<ApiError> handleBusinessException(BusinessException ex, WebRequest request) {
		ApiError apiError = new ApiError(ex.getStatus(), ex.getMessage(), ex.getDebugMessage());
		apiError.setPath(((ServletWebRequest) request).getRequest().getRequestURI());
		log.warn("Business exception: {}", ex.getMessage());
		return new ResponseEntity<>(apiError, ex.getStatus());
	}

	// Data integrity violation (unique constraints, etc.)
	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<ApiError> handleDataIntegrityViolation(DataIntegrityViolationException ex,
			WebRequest request) {
		ApiError apiError = new ApiError(HttpStatus.CONFLICT, "Violation d'intégrité des données",
				"La requête viole une contrainte d'intégrité");
		apiError.setPath(((ServletWebRequest) request).getRequest().getRequestURI());
		log.error("Data integrity violation: {}", ex.getMessage(), ex);
		return new ResponseEntity<>(apiError, HttpStatus.CONFLICT);
	}

	// Access denied
	/*
	 * @ExceptionHandler(AccessDeniedException.class) public
	 * ResponseEntity<ApiError> handleAccessDenied(AccessDeniedException ex,
	 * WebRequest request) { ApiError apiError = new ApiError(HttpStatus.FORBIDDEN,
	 * "Accès refusé", "Vous n'avez pas les permissions nécessaires");
	 * apiError.setPath(((ServletWebRequest) request).getRequest().getRequestURI());
	 * log.warn("Access denied: {}", ex.getMessage()); return new
	 * ResponseEntity<>(apiError, HttpStatus.FORBIDDEN); }
	 */

	// Constraint violation (JPA)
	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<ApiError> handleConstraintViolation(ConstraintViolationException ex, WebRequest request) {
		ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, "Violation de contrainte");

		apiError.setSubErrors(ex.getConstraintViolations().stream()
				.map(violation -> new ApiError.ApiSubError(violation.getPropertyPath().toString(),
						violation.getInvalidValue(), violation.getMessage()))
				.collect(Collectors.toList()));

		apiError.setPath(((ServletWebRequest) request).getRequest().getRequestURI());
		log.warn("Constraint violation: {}", ex.getMessage());

		return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
	}

	// All other exceptions
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiError> handleAllUncaughtException(Exception ex, WebRequest request) {
		ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, "Erreur interne du serveur",
				"Une erreur inattendue s'est produite");
		apiError.setPath(((ServletWebRequest) request).getRequest().getRequestURI());

		log.error("Unexpected error: {}", ex.getMessage(), ex);

		return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	
	@ExceptionHandler(AuthenticationException.class)
	public ResponseEntity<ApiError> handleAuthenticationException(
	        AuthenticationException ex,
	        HttpServletRequest request) {

	    ApiError error = new ApiError(
	            HttpStatus.UNAUTHORIZED,
	            "Email ou mot de passe incorrect"
	    );

	    error.setPath(request.getRequestURI());

	    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
	}
}
