package com.project.triplog.service;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.project.triplog.domain.User;
import com.project.triplog.dto.user.JoinRequest;
import com.project.triplog.global.exception.CustomException;
import com.project.triplog.global.exception.ErrorCode;
import com.project.triplog.repository.UserRepository;
import com.project.triplog.security.JwtTokenProvider;

class UserServiceTest {
	private UserRepository userRepository;
	private PasswordEncoder passwordEncoder;
	private EmailVerificationService emailVerificationService;
	private JwtTokenProvider jwtTokenProvider;
	private UserService userService;

	@BeforeEach
	void setUp() {
		userRepository = mock(UserRepository.class);
		passwordEncoder = mock(PasswordEncoder.class);
		emailVerificationService = mock(EmailVerificationService.class);
		jwtTokenProvider = mock(JwtTokenProvider.class);
		userService = new UserService(userRepository, passwordEncoder, emailVerificationService, jwtTokenProvider);
	}

	@Test
	@DisplayName("사용자명이 이미 존재하는 경우 true 반환")
	void shouldReturnTrueWhenUsernameExists() {
		String username = "existingUser";
		when(userRepository.existsByUserId(username)).thenReturn(true);

		boolean result = userService.isExistUsername(username);

		assertThat(result).isTrue();
	}

	@Test
	@DisplayName("사용자명이 존재하지 않는 경우 false 반환")
	void shouldReturnFalseWhenUsernameDoesNotExist() {
		String username = "newUser";
		when(userRepository.existsByUserId(username)).thenReturn(false);

		boolean result = userService.isExistUsername(username);

		assertThat(result).isFalse();
	}

	@Test
	@DisplayName("회원가입 성공")
	void shouldJoinSuccessfully() {
		JoinRequest joinRequest = JoinRequest.builder()
			.userId("newUser")
			.email("test@example.com")
			.password("password123")
			.build();

		when(userRepository.existsByUserId("newUser")).thenReturn(false);
		when(userRepository.existsByEmail("test@example.com")).thenReturn(false);
		when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");

		userService.join(joinRequest);

		verify(emailVerificationService).checkVerified("test@example.com");
		verify(userRepository).save(any(User.class));
		verify(emailVerificationService).deleteVerification("test@example.com");
	}

	@Test
	@DisplayName("중복된 사용자명으로 회원가입 시 예외 발생")
	void shouldThrowExceptionWhenUsernameIsDuplicated() {
		JoinRequest joinRequest = JoinRequest.builder()
			.userId("existingUser")
			.email("test@example.com")
			.password("password123")
			.build();

		when(userRepository.existsByUserId("existingUser")).thenReturn(true);

		assertThatThrownBy(() -> userService.join(joinRequest))
			.isInstanceOf(CustomException.class)
			.hasFieldOrPropertyWithValue("errorCode", ErrorCode.DUPLICATE_USERNAME);
	}

	@Test
	@DisplayName("중복된 이메일로 회원가입 시 예외 발생")
	void shouldThrowExceptionWhenEmailIsDuplicated() {
		JoinRequest joinRequest = JoinRequest.builder()
			.userId("newUser")
			.email("existing@example.com")
			.password("password123")
			.build();

		when(userRepository.existsByUserId("newUser")).thenReturn(false);
		when(userRepository.existsByEmail("existing@example.com")).thenReturn(true);

		assertThatThrownBy(() -> userService.join(joinRequest))
			.isInstanceOf(CustomException.class)
			.hasFieldOrPropertyWithValue("errorCode", ErrorCode.DUPLICATE_EMAIL);
	}
}
