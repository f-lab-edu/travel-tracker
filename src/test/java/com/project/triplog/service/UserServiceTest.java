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
	@DisplayName("사용자명이 이미 존재하는 경우 true 반환")
	void shouldReturnTrueWhenUsernameExists() {
		String username = "existingUser";
		when(userRepository.existsByUsername(username)).thenReturn(true);

		boolean result = userService.isExistUsername(username);

		assertThat(result).isTrue();
	}

	@Test
	@DisplayName("사용자명이 존재하지 않는 경우 false 반환")
	void shouldReturnFalseWhenUsernameDoesNotExist() {
		String username = "newUser";
		when(userRepository.existsByUsername(username)).thenReturn(false);

		boolean result = userService.isExistUsername(username);

		assertThat(result).isFalse();
	}

	@Test
	@DisplayName("정상적인 회원가입 시 저장 및 인증정보 삭제")
	void shouldJoinSuccessfully() {
		JoinRequest joinRequest = new JoinRequest("newUser", "홍길동", "test@example.com", "password123");

		when(userRepository.existsByUsername(anyString())).thenReturn(false);
		when(userRepository.existsByEmail(anyString())).thenReturn(false);
		when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
		doNothing().when(emailVerificationService).checkVerified(anyString());

		userService.join(joinRequest);

		verify(userRepository, times(1)).save(any(User.class));
		verify(emailVerificationService, times(1)).deleteVerification(joinRequest.getEmail());
	}

	@Test
	@DisplayName("중복된 사용자명으로 회원가입 시 예외 발생")
	void shouldThrowExceptionWhenUsernameExists() {
		JoinRequest joinRequest = new JoinRequest("existingUser", "홍길동", "test@example.com", "password123");

		when(userRepository.existsByUsername(anyString())).thenReturn(true);
		doNothing().when(emailVerificationService).checkVerified(anyString());

		assertThatThrownBy(() -> userService.join(joinRequest))
			.isInstanceOf(ApiException.class)
			.hasMessage("이미 존재하는 사용자 이름입니다.");
	}

	@Test
	@DisplayName("중복된 이메일로 회원가입 시 예외 발생")
	void shouldThrowExceptionWhenEmailExists() {
		JoinRequest joinRequest = new JoinRequest("newUser", "홍길동", "existing@example.com", "password123");

		when(userRepository.existsByUsername(anyString())).thenReturn(false);
		when(userRepository.existsByEmail(anyString())).thenReturn(true);
		doNothing().when(emailVerificationService).checkVerified(anyString());

		assertThatThrownBy(() -> userService.join(joinRequest))
			.isInstanceOf(ApiException.class)
			.hasMessage("이미 존재하는 이메일입니다.");
	}

	@Test
	@DisplayName("회원가입 시 비밀번호가 인코딩되는지 확인")
	void shouldEncodePasswordDuringJoin() {
		JoinRequest joinRequest = new JoinRequest("newUser", "userName", "test@example.com", "password");

		when(userRepository.existsByUsername(anyString())).thenReturn(false);
		when(userRepository.existsByEmail(anyString())).thenReturn(false);
		when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
		doNothing().when(emailVerificationService).checkVerified(anyString());

		userService.join(joinRequest);

		verify(passwordEncoder, times(1)).encode("password");
	}

	@Test
	@DisplayName("이메일 인증이 완료되지 않은 경우 예외 발생")
	void shouldThrowExceptionWhenEmailNotVerified() {
		JoinRequest joinRequest = new JoinRequest("newUser", "홍길동", "unverified@example.com", "password123");

		doThrow(new ApiException(ErrorCode.EMAIL_NOT_VERIFIED))
			.when(emailVerificationService).checkVerified(joinRequest.getEmail());

		assertThatThrownBy(() -> userService.join(joinRequest))
			.isInstanceOf(ApiException.class);
	}

}
