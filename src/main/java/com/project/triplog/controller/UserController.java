package com.project.triplog.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.triplog.dto.user.EmailRequest;
import com.project.triplog.dto.user.JoinRequest;
import com.project.triplog.dto.user.LoginRequest;
import com.project.triplog.dto.user.LoginResponse;
import com.project.triplog.global.response.ApiResponse;
import com.project.triplog.service.EmailVerificationService;
import com.project.triplog.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {
	private final UserService userService;
	private final EmailVerificationService emailVerificationService;

	@GetMapping("/users/validation")
	public ApiResponse<Boolean> checkUsername(@RequestParam String username) {
		boolean isExists = userService.isExistUsername(username);
		return ApiResponse.success("사용자명 중복 검사가 완료되었습니다", isExists);
	}

	@PostMapping("/users/join")
	public ResponseEntity<ApiResponse<Void>> join(@Valid @RequestBody JoinRequest joinRequest) {
		userService.join(joinRequest);
		return ResponseEntity.status(HttpStatus.CREATED)
			.body(ApiResponse.success("회원가입이 완료되었습니다"));
	}

	@PostMapping("/users/email/verification")
	public ApiResponse<Void> sendVerificationEmail(@RequestBody EmailRequest emailRequest) {
		emailVerificationService.sendVerificationEmail(emailRequest);
		return ApiResponse.success("인증 이메일이 발송되었습니다");
	}

	@GetMapping("/users/email/verification")
	public ApiResponse<Void> verificationEmail(@RequestParam String email, @RequestParam String token) {
		emailVerificationService.verifyToken(email, token);
		return ApiResponse.success("이메일 인증이 완료되었습니다");
	}

	@PostMapping("/users/login")
	public ApiResponse<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
		LoginResponse loginResponse = userService.login(loginRequest);
		return ApiResponse.success("로그인이 완료되었습니다", loginResponse);
	}
}
