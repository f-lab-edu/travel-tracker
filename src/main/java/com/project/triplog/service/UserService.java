package com.project.triplog.service;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.project.triplog.domain.User;
import com.project.triplog.dto.JoinRequest;
import com.project.triplog.dto.LoginRequest;
import com.project.triplog.dto.LoginResponse;
import com.project.triplog.exception.DuplicationException;
import com.project.triplog.repository.UserRepository;
import com.project.triplog.security.JwtTokenProvider;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final EmailVerificationService emailVerificationService;
	private final JwtTokenProvider jwtTokenProvider;

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

	public LoginResponse login(LoginRequest loginRequest) {
		User user = userRepository.findByUserId(loginRequest.getUserId())
			.orElseThrow(() -> new BadCredentialsException("사용자를 찾을 수 없습니다."));

		if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
			throw new BadCredentialsException("비밀번호가 일치하지 않습니다.");
		}

		String token = jwtTokenProvider.createToken(user.getUserId());
		return LoginResponse.of(token);
	}
}
