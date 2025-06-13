package com.project.triplog.service;

import org.springframework.stereotype.Service;

import com.project.triplog.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
	private final UserRepository userRepository;

	public boolean isExistUsername(String username) {
		return userRepository.existsByUsername(username);
	}
}
