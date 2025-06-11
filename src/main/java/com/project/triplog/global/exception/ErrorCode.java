package com.project.triplog.global.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
	UNKNOWN_ERROR("UNKNOWN_ERROR", "알 수 없는 오류가 발생하였습니다."),
	INVALID_VALUE("INVALID_VALUE", "유효하지 않은 값이 입력되었습니다."),
	NOT_FOUND("NOT_FOUND", "요청한 리소스를 찾을 수 없습니다.");;

	private final String code;
	private final String message;

	ErrorCode(String code, String message) {
		this.code = code;
		this.message = message;
	}
}
