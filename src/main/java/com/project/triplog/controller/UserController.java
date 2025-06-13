package com.project.triplog.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.triplog.service.UserService;

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

}
