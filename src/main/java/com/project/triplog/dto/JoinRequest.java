package com.project.triplog.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class JoinRequest {
	@NotBlank(message = "사용자 아이디는 필수 입력 항목입니다.")
	private final String userId;
	@NotBlank(message = "이름은 필수 입력 항목입니다.")
	private final String name;
	@Email(message = "유효한 이메일 형식이 아닙니다.")
	@NotBlank(message = "이메일은 필수 입력 항목입니다.")
	private final String email;
	@NotBlank(message = "비밀번호는 필수 입력 항목입니다.")
	@Size(min = 8, max = 20, message = "비밀번호는 8자 이상 20자 이하로 입력해야 합니다.")
	private final String password;
}
