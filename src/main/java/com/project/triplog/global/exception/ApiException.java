package com.project.triplog.global.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class ApiException extends RuntimeException {
	private final HttpStatus status;
	private final String errorCode;
	private final ErrorCode errorCodeEnum;

	public ApiException(ErrorCode errorCode) {
		super(errorCode.getMessage());
		this.status = errorCode.getHttpStatus();
		this.errorCode = errorCode.getCode();
		this.errorCodeEnum = errorCode;
	}

	public ApiException(ErrorCode errorCode, String customMessage) {
		super(customMessage);
		this.status = errorCode.getHttpStatus();
		this.errorCode = errorCode.getCode();
		this.errorCodeEnum = errorCode;
	}
}
