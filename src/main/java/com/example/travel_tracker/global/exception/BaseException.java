package com.example.travel_tracker.global.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class BaseException extends RuntimeException {
	private final HttpStatus status;
	private final String errorCode;

	public BaseException(HttpStatus status, String errorCode, String message) {
		super(message);
		this.status = status;
		this.errorCode = errorCode;
	}
}
