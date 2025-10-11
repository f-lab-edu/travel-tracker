package com.project.triplog.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.triplog.dto.EmailCheckRequest;
import com.project.triplog.dto.EmailRequest;
import com.project.triplog.dto.JoinRequest;
import com.project.triplog.dto.UsernameCheckRequest;
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

	@PostMapping("/users/username/exists")
	public ResponseEntity<Boolean> checkUsernameExists(@Valid @RequestBody UsernameCheckRequest request) {
		boolean isExists = userService.isExistUsername(request.getUsername());
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

	@PostMapping("/users/email/verification")
	public ResponseEntity<Void> sendVerificationEmail(@RequestBody EmailRequest emailRequest) {
		emailVerificationService.sendVerificationEmail(emailRequest);
		return ResponseEntity.ok().build();
	}

	@GetMapping("/users/email/verification")
	public ResponseEntity<Void> verificationEmail(@RequestParam String email, @RequestParam String token) {
		emailVerificationService.verifyToken(email, token);
		return ResponseEntity.ok().build();
	}
}
