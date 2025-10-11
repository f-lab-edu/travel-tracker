package com.project.triplog.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.triplog.dto.user.EmailCheckRequest;
import com.project.triplog.dto.user.EmailRequest;
import com.project.triplog.dto.user.EmailVerificationRequest;
import com.project.triplog.dto.user.JoinRequest;
import com.project.triplog.dto.user.LoginRequest;
import com.project.triplog.dto.user.LoginResponse;
import com.project.triplog.dto.user.UserIdCheckRequest;
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

	@PostMapping("/users/userid/exists")
	public ApiResponse<Boolean> checkUserIdExists(@Valid @RequestBody UserIdCheckRequest request) {
		boolean isExists = userService.isExistUserId(request.getUserId());
		return ApiResponse.success("사용자 ID 중복 검사가 완료되었습니다", isExists);
	}

	@PostMapping("/users/email/exists")
	public ApiResponse<Boolean> checkEmailExists(@Valid @RequestBody EmailCheckRequest request) {
		boolean isExists = userService.isExistEmail(request.getEmail());
		return ApiResponse.success("이메일 중복 검사가 완료되었습니다", isExists);
	}

	@PostMapping("/users/join")
	public ResponseEntity<ApiResponse<Void>> join(@Valid @RequestBody JoinRequest joinRequest) {
		userService.join(joinRequest);
		return ResponseEntity.status(HttpStatus.CREATED)
			.body(ApiResponse.success("회원가입이 완료되었습니다"));
	}

	@PostMapping("/users/email/request-verification")
	public ApiResponse<Void> requestEmailVerification(@RequestBody EmailRequest emailRequest) {
		emailVerificationService.sendVerificationEmail(emailRequest);
		return ApiResponse.success("인증 이메일이 발송되었습니다");
	}

	@PostMapping("/users/email/verification")
	public ApiResponse<Void> verifyEmail(@Valid @RequestBody EmailVerificationRequest request) {
		emailVerificationService.verifyToken(request.getEmail(), request.getToken());
		return ApiResponse.success("이메일 인증이 완료되었습니다");
	}

	@PostMapping("/users/login")
	public ApiResponse<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
		LoginResponse loginResponse = userService.login(loginRequest);
		return ApiResponse.success("로그인이 완료되었습니다", loginResponse);
	}
}
