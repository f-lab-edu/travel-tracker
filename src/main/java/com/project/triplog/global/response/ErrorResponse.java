package com.project.triplog.global.response;

import java.util.Collections;
import java.util.List;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.project.triplog.global.exception.FieldErrorDetail;

import lombok.Getter;

@Getter
public class ErrorResponse extends BaseResponse {
	private final HttpStatus status;
	private final String errorCode;
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	private final List<FieldErrorDetail> errorDetails;

	private ErrorResponse(HttpStatus status, String errorCode, String message, List<FieldErrorDetail> errorDetails) {
		super(false, message);
		this.status = status;
		this.errorCode = errorCode;
		this.errorDetails = errorDetails;
	}

	public static ErrorResponse of(HttpStatus status, String errorCode, String message) {
		return new ErrorResponse(status, errorCode, message, Collections.emptyList());
	}

	public static ErrorResponse of(HttpStatus status, String errorCode, String message,
		List<FieldErrorDetail> errorDetails) {
		return new ErrorResponse(status, errorCode, message, errorDetails);
	}
}
