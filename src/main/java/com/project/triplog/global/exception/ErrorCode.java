package com.project.triplog.global.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
    UNKNOWN_ERROR("UNKNOWN_ERROR", "알 수 없는 오류가 발생하였습니다."),
    INVALID_VALUE("INVALID_VALUE", "유효하지 않은 값이 입력되었습니다."),
    NOT_FOUND("NOT_FOUND", "요청한 리소스를 찾을 수 없습니다."),
    DUPLICATION("DUPLICATION", "이미 존재하는 값입니다."),
    EMAIL_NOT_VERIFIED("EMAIL_NOT_VERIFIED", "이메일 인증이 완료되지 않았습니다."),
    PARAMETER_MISSING("PARAMETER_MISSING", "필수 요청 파라미터가 누락되었습니다."),
    EMAIL_SEND_FAILURE("EMAIL_SEND_FAILURE", "이메일 전송에 실패하였습니다."),
    ;

    private final String code;
    private final String message;

    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
