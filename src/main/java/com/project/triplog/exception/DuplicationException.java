package com.project.triplog.exception;

import org.springframework.http.HttpStatus;

import com.project.triplog.global.exception.BaseException;
import com.project.triplog.global.exception.ErrorCode;

public class DuplicationException extends BaseException {
	public DuplicationException(String message) {
		super(HttpStatus.CONFLICT, ErrorCode.DUPLICATION.getCode(), message);
	}
}
