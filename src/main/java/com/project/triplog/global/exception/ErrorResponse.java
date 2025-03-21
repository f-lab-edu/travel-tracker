package com.project.triplog.global.exception;

import org.springframework.http.HttpStatus;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ErrorResponse {
	private final HttpStatus status;
	private final String errorCode;
	private final String message;

	public static ErrorResponse of(HttpStatus status, String errorCode, String message) {
		return new ErrorResponse(status, errorCode, message);
	}
}
