package com.project.triplog.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserIdCheckRequest {

	@NotBlank(message = "사용자 아이디는 필수입니다.")
	private String userId;
}
