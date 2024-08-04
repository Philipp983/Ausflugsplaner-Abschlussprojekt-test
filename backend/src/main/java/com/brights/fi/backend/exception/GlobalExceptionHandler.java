package com.brights.fi.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException ex) {
		ErrorResponse errorResponse = new ErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage(), LocalDateTime.now());
		return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
		ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), LocalDateTime.now());
		return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(BusinessLogicException.class)
	public ResponseEntity<ErrorResponse> handleBusinessLogicException(BusinessLogicException ex) {
		ErrorResponse errorResponse = new ErrorResponse(HttpStatus.CONFLICT, ex.getMessage(), LocalDateTime.now());
		return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
		ErrorResponse errorResponse = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Ein interner Serverfehler ist aufgetreten", LocalDateTime.now());
		return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	}


//  Meine Implementierung
//	@ExceptionHandler(ResourceNotFoundException.class)
//	public final ResponseEntity<Object> auffangeResourceNotFoundException(ResourceNotFoundException rnfEx) {
//		// Zeile lässt sich sparen und direkt in response entity einfügen
////		ResourceNotFoundException ausnahmeAntwort = new ResourceNotFoundException(rnfEx.getMessage());
//		return new ResponseEntity<>(rnfEx.getMessage(), HttpStatus.NOT_FOUND);
//	}

	/*

	Unterschied hier: Hashmap. ermöglicht es mir, die error message weiter zu 'verfeinern'. Im Beispiel hier:
	wurde die error message einem 'property' message zugewiesen (so wird es später json als field gelesen)

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<Map<String, String>> handleResourceNotFoundException(ResourceNotFoundException reEx) {
		Map<String, String> error = new HashMap<>();
		error.put("message", reEx.getMessage());
		return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
	}

	 */

	class ErrorResponse {
		private HttpStatus status;
		private String message;
		private LocalDateTime errorZeitpunkt;

		public ErrorResponse(HttpStatus status, String message, LocalDateTime errorZeitpunkt) {

			this.status = status;
			this.message = message;
			this.errorZeitpunkt = errorZeitpunkt;
		}

		public HttpStatus getStatus() {
			return status;
		}

		public void setStatus(HttpStatus status) {
			this.status = status;
		}

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}

		public void setErrorZeitpunkt(LocalDateTime errorZeitpunkt) {
			this.errorZeitpunkt = errorZeitpunkt;
		}
	}

}
