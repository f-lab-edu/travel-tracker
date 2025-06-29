package com.project.triplog.dto;

import lombok.Getter;

@Getter
public class LoginResponse {
	private String token;

	public static LoginResponse of(String token) {
		LoginResponse loginResponse = new LoginResponse();
		loginResponse.token = token;
		return loginResponse;
	}
}
