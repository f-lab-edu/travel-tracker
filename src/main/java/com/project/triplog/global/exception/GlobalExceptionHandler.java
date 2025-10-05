package com.project.triplog.global.exception;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(BaseException.class)
	public ResponseEntity<ErrorResponse> handleBaseException(BaseException exception) {
		ErrorResponse errorResponse = ErrorResponse.of(exception.getStatus(), exception.getErrorCode(),
			exception.getMessage());

		return ResponseEntity.status(exception.getStatus()).body(errorResponse);
	}

	@ExceptionHandler(NoHandlerFoundException.class)
	public ResponseEntity<ErrorResponse> handleNoHandlerFoundException(NoHandlerFoundException exception) {
		ErrorResponse errorResponse = ErrorResponse.of(HttpStatus.NOT_FOUND, ErrorCode.NOT_FOUND.getCode(),
			ErrorCode.NOT_FOUND.getMessage());

		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(
		MethodArgumentNotValidException exception) {
		List<FieldErrorDetail> fieldErrors = exception.getBindingResult().getFieldErrors().stream()
			.map(error -> FieldErrorDetail.of(error.getField(), error.getDefaultMessage()))
			.collect(Collectors.toList());

		ErrorResponse errorResponse = ErrorResponse.of(HttpStatus.BAD_REQUEST, ErrorCode.INVALID_VALUE.getCode(),
			ErrorCode.INVALID_VALUE.getMessage(), fieldErrors);

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleUnknownException() {
		ErrorResponse errorResponse = ErrorResponse.of(HttpStatus.INTERNAL_SERVER_ERROR,
			ErrorCode.UNKNOWN_ERROR.getCode(), ErrorCode.UNKNOWN_ERROR.getMessage());

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
	}
}
