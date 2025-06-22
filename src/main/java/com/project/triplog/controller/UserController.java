package com.project.triplog.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.triplog.dto.JoinRequest;
import com.project.triplog.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class UserController {
	private final UserService userService;

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
}
