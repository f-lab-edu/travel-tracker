package com.project.triplog.global.exception;

public enum ErrorCode {
	UNKNOWN_ERROR("UNKNOWN_ERROR", "알 수 없는 오류가 발생하였습니다.");

	private final String code;
	private final String message;

	ErrorCode(String code, String message) {
		this.code = code;
		this.message = message;
	}

	public String getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}
}
