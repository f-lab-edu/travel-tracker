package com.project.triplog.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.triplog.dto.EmailCheckRequest;
import com.project.triplog.dto.EmailRequest;
import com.project.triplog.dto.EmailVerificationRequest;
import com.project.triplog.dto.JoinRequest;
import com.project.triplog.dto.UserIdCheckRequest;
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
	public ResponseEntity<Boolean> checkUserIdExists(@Valid @RequestBody UserIdCheckRequest request) {
		boolean isExists = userService.isExistUserId(request.getUserId());
		return ResponseEntity.ok(isExists);
	}

	@PostMapping("/users/email/exists")
	public ResponseEntity<Boolean> checkEmailExists(@Valid @RequestBody EmailCheckRequest request) {
		boolean isExists = userService.isExistEmail(request.getEmail());
		return ResponseEntity.ok(isExists);
	}

	@PostMapping("/users/join")
	public ResponseEntity<Void> join(@Valid @RequestBody JoinRequest joinRequest) {
		userService.join(joinRequest);
		return ResponseEntity.ok().build();
	}

	@PostMapping("/users/email/request-verification")
	public ResponseEntity<Void> requestEmailVerification(@RequestBody EmailRequest emailRequest) {
		emailVerificationService.sendVerificationEmail(emailRequest);
		return ResponseEntity.ok().build();
	}

	@PostMapping("/users/email/verification")
	public ResponseEntity<Void> verifyEmail(@Valid @RequestBody EmailVerificationRequest request) {
		emailVerificationService.verifyToken(request.getEmail(), request.getToken());
		return ResponseEntity.ok().build();
	}
}
