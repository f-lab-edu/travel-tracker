package com.project.triplog.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class FieldErrorDetail {
	private final String field;
	private final String message;

	public static FieldErrorDetail of(String field, String message) {
		return new FieldErrorDetail(field, message);
	}
}
