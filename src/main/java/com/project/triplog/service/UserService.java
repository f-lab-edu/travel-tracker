package com.project.triplog.service;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.project.triplog.domain.User;
import com.project.triplog.dto.user.JoinRequest;
import com.project.triplog.dto.user.LoginRequest;
import com.project.triplog.dto.user.LoginResponse;
import com.project.triplog.global.exception.CustomException;
import com.project.triplog.global.exception.ErrorCode;
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

	public boolean isExistUsername(String username) {
		return userRepository.existsByUserId(username);
	}

	public void join(JoinRequest joinRequest) {
		emailVerificationService.checkVerified(joinRequest.getEmail());
		if (isExistUsername(joinRequest.getUserId())) {
			throw new CustomException(ErrorCode.DUPLICATE_USERNAME);
		}
		if (userRepository.existsByEmail(joinRequest.getEmail())) {
			throw new CustomException(ErrorCode.DUPLICATE_EMAIL);
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
			.orElseThrow(() -> new BadCredentialsException("아이디 또는 비밀번호가 틀렸습니다."));

		if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
			throw new BadCredentialsException("아이디 또는 비밀번호가 틀렸습니다.");
		}

		String token = jwtTokenProvider.generateToken(user.getUserId());

		return LoginResponse.builder()
			.token(token)
			.build();
	}
}
