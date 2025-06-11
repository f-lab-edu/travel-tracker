package com.project.triplog.global.exception;

import java.util.Collections;
import java.util.List;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ErrorResponse {
	private final HttpStatus status;
	private final String errorCode;
	private final String message;
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	private final List<FieldErrorDetail> errorDetails;

	public static ErrorResponse of(HttpStatus status, String errorCode, String message) {
		return new ErrorResponse(status, errorCode, message, Collections.emptyList());
	}

	public static ErrorResponse of(HttpStatus status, String errorCode, String message,
		List<FieldErrorDetail> errorDetails) {
		return new ErrorResponse(status, errorCode, message, errorDetails);
	}
}
