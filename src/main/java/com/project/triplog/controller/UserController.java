package com.project.triplog.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.triplog.dto.EmailRequest;
import com.project.triplog.dto.JoinRequest;
import com.project.triplog.dto.LoginRequest;
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
    public ResponseEntity<Boolean> checkUsername(@RequestParam String username) {
        boolean isExists = userService.isExistUsername(username);
        return ResponseEntity.ok(isExists);
    }

    @PostMapping("/users/join")
    public ResponseEntity join(@Valid @RequestBody JoinRequest joinRequest) {
        userService.join(joinRequest);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/users/email/verification")
    public ResponseEntity sendVerificationEmail(@RequestBody EmailRequest emailRequest) {
        emailVerificationService.sendVerificationEmail(emailRequest);
        return ResponseEntity.ok().build();
    }

	@GetMapping("/users/email/verification")
	public ResponseEntity verificationEmail(@RequestParam String email, @RequestParam String token) {
		emailVerificationService.verifyToken(email, token);
		return ResponseEntity.ok().build();
	}

	@PostMapping("/users/login")
	public ResponseEntity login(@Valid @RequestBody LoginRequest loginRequest) {
		userService.login(loginRequest);
		return ResponseEntity.ok().build();
	}
}
