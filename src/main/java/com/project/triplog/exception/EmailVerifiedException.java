package com.project.triplog.exception;

import org.springframework.http.HttpStatus;

import com.project.triplog.global.exception.BaseException;
import com.project.triplog.global.exception.ErrorCode;

public class EmailVerifiedException extends BaseException {
	public EmailVerifiedException() {
		super(HttpStatus.BAD_REQUEST, ErrorCode.EMAIL_NOT_VERIFIED.getCode(),
			ErrorCode.EMAIL_NOT_VERIFIED.getMessage());
	}
}
