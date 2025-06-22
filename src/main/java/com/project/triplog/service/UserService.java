package com.project.triplog.service;

import org.springframework.stereotype.Service;

import com.project.triplog.domain.User;
import com.project.triplog.dto.JoinRequest;
import com.project.triplog.exception.DuplicationException;
import com.project.triplog.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
	private final UserRepository userRepository;

	public boolean isExistUsername(String username) {
		return userRepository.existsByUsername(username);
	}

	public void join(JoinRequest joinRequest) {
		if (isExistUsername(joinRequest.getUsername())) {
			throw new DuplicationException("이미 존재하는 사용자 이름입니다.");
		}
		if (isExistEmail(joinRequest.getEmail())) {
			throw new DuplicationException("이미 존재하는 이메일입니다.");
		}
		User user = User.from(joinRequest);
		userRepository.save(user);
	}

	private boolean isExistEmail(String email) {
		return userRepository.existsByEmail(email);
	}
}
