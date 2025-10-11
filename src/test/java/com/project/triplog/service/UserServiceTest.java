package com.project.triplog.service;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.project.triplog.domain.User;
import com.project.triplog.dto.user.JoinRequest;
import com.project.triplog.global.exception.ApiException;
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
	@DisplayName("사용자 ID가 이미 존재하는 경우 true 반환")
	void shouldReturnTrueWhenUserIdExists() {
		String userId = "existingUser";
		when(userRepository.existsByUserId(userId)).thenReturn(true);

		boolean result = userService.isExistUserId(userId);

		assertThat(result).isTrue();
	}

	@Test
	@DisplayName("사용자 ID가 존재하지 않는 경우 false 반환")
	void shouldReturnFalseWhenUserIdDoesNotExist() {
		String userId = "newUser";
		when(userRepository.existsByUserId(userId)).thenReturn(false);

		boolean result = userService.isExistUserId(userId);

		assertThat(result).isFalse();
	}

	@Test
	@DisplayName("회원가입 성공")
	void shouldJoinSuccessfully() {
		JoinRequest joinRequest = new JoinRequest("newUser", "테스트", "test@example.com", "password123");

		when(userRepository.existsByUserId("newUser")).thenReturn(false);
		when(userRepository.existsByEmail("test@example.com")).thenReturn(false);
		when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");

		userService.join(joinRequest);

		verify(emailVerificationService).checkVerified("test@example.com");
		verify(userRepository).save(any(User.class));
		verify(emailVerificationService).deleteVerification("test@example.com");
	}

	@Test
	@DisplayName("중복된 사용자 ID로 회원가입 시 예외 발생")
	void shouldThrowExceptionWhenUserIdIsDuplicated() {
		JoinRequest joinRequest = new JoinRequest("existingUser", "테스트", "test@example.com", "password123");

		when(userRepository.existsByUserId("existingUser")).thenReturn(true);

		assertThatThrownBy(() -> userService.join(joinRequest))
			.isInstanceOf(ApiException.class)
			.hasFieldOrPropertyWithValue("errorCodeEnum", ErrorCode.DUPLICATION)
			.hasMessageContaining("이미 존재하는 사용자 아이디입니다");
	}

	@Test
	@DisplayName("중복된 이메일로 회원가입 시 예외 발생")
	void shouldThrowExceptionWhenEmailIsDuplicated() {
		JoinRequest joinRequest = new JoinRequest("newUser", "테스트", "existing@example.com", "password123");

		when(userRepository.existsByUserId("newUser")).thenReturn(false);
		when(userRepository.existsByEmail("existing@example.com")).thenReturn(true);

		assertThatThrownBy(() -> userService.join(joinRequest))
			.isInstanceOf(ApiException.class)
			.hasFieldOrPropertyWithValue("errorCodeEnum", ErrorCode.DUPLICATION)
			.hasMessageContaining("이미 존재하는 이메일입니다");
	}
}
