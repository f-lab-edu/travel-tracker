package com.project.triplog.global.response;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;

@Getter
public abstract class BaseResponse {
	private final boolean success;
	private final String message;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private final LocalDateTime timestamp;

	protected BaseResponse(boolean success, String message) {
		this.success = success;
		this.message = message;
		this.timestamp = LocalDateTime.now();
	}
}
