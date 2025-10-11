package com.project.triplog.service;

import org.springframework.security.crypto.password.PasswordEncoder;
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
	private final PasswordEncoder passwordEncoder;
	private final EmailVerificationService emailVerificationService;

	public boolean isExistUserId(String userId) {
		return userRepository.existsByUserId(userId);
	}

	public boolean isExistEmail(String email) {
		return userRepository.existsByEmail(email);
	}

	public void join(JoinRequest joinRequest) {
		emailVerificationService.checkVerified(joinRequest.getEmail());
		if (isExistUserId(joinRequest.getUserId())) {
			throw new DuplicationException("이미 존재하는 사용자 아이디입니다.");
		}
		if (isExistEmail(joinRequest.getEmail())) {
			throw new DuplicationException("이미 존재하는 이메일입니다.");
		}

		String newPassword = encodingPassword(joinRequest.getPassword());
		User user = User.from(joinRequest, newPassword);
		userRepository.save(user);

		emailVerificationService.deleteVerification(joinRequest.getEmail());
	}

	private String encodingPassword(String password) {
		return passwordEncoder.encode(password);
	}
}
