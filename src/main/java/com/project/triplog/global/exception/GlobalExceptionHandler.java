package com.project.triplog.global.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(BaseException.class)
	public ResponseEntity<ErrorResponse> handleBaseException(BaseException exception) {
		ErrorResponse errorResponse = ErrorResponse.of(exception.getStatus(), exception.getErrorCode(),
			exception.getMessage(), exception.getClass().getName());

		return ResponseEntity.status(exception.getStatus()).body(errorResponse);
	}
}
