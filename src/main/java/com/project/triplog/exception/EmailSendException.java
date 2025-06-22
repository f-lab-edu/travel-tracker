package com.project.triplog.exception;

import com.project.triplog.global.exception.BaseException;
import com.project.triplog.global.exception.ErrorCode;
import org.springframework.http.HttpStatus;

public class EmailSendException extends BaseException {
    public EmailSendException() {
        super(HttpStatus.BAD_GATEWAY, ErrorCode.EMAIL_NOT_VERIFIED.getCode(),
                ErrorCode.EMAIL_NOT_VERIFIED.getMessage());
    }
}
