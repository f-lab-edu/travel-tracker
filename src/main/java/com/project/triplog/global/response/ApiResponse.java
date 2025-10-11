package com.project.triplog.global.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;

@Getter
public class ApiResponse<T> extends BaseResponse {
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private final T data;

	private ApiResponse(String message, T data) {
		super(true, message);
		this.data = data;
	}

	// 데이터가 있는 성공 응답
	public static <T> ApiResponse<T> success(String message, T data) {
		return new ApiResponse<>(message, data);
	}

	// 데이터가 없는 성공 응답
	public static ApiResponse<Void> success(String message) {
		return new ApiResponse<>(message, null);
	}

	// 기본 성공 메시지와 데이터
	public static <T> ApiResponse<T> success(T data) {
		return new ApiResponse<>("요청이 성공적으로 처리되었습니다", data);
	}

	// 기본 성공 메시지만
	public static ApiResponse<Void> success() {
		return new ApiResponse<>("요청이 성공적으로 처리되었습니다", null);
	}
}
