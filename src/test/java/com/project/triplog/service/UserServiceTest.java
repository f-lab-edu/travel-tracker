package com.project.triplog.service;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.project.triplog.domain.User;
import com.project.triplog.dto.JoinRequest;
import com.project.triplog.exception.DuplicationException;
import com.project.triplog.exception.EmailVerifiedException;
import com.project.triplog.repository.UserRepository;

class UserServiceTest {
	private UserRepository userRepository;
	private PasswordEncoder passwordEncoder;
	private EmailVerificationService emailVerificationService;
	private UserService userService;

	@BeforeEach
	void setUp() {
		userRepository = mock(UserRepository.class);
		passwordEncoder = mock(PasswordEncoder.class);
		emailVerificationService = mock(EmailVerificationService.class);
		userService = new UserService(userRepository, passwordEncoder, emailVerificationService);
	}

	@Test
	@DisplayName("사용자 아이디가 이미 존재하는 경우 true 반환")
	void shouldReturnTrueWhenUserIdExists() {
		String userId = "existingUser";
		when(userRepository.existsByUserId(userId)).thenReturn(true);

		boolean result = userService.isExistUserId(userId);

		assertThat(result).isTrue();
	}

	@Test
	@DisplayName("사용자 아이디가 존재하지 않는 경우 false 반환")
	void shouldReturnFalseWhenUserIdDoesNotExist() {
		String userId = "newUser";
		when(userRepository.existsByUserId(userId)).thenReturn(false);

		boolean result = userService.isExistUserId(userId);

		assertThat(result).isFalse();
	}

	@Test
	@DisplayName("정상적인 회원가입 시 저장 및 인증정보 삭제")
	void shouldJoinSuccessfully() {
		JoinRequest joinRequest = new JoinRequest("newUser", "홍길동", "test@example.com", "password123");

		when(userRepository.existsByUserId(anyString())).thenReturn(false);
		when(userRepository.existsByEmail(anyString())).thenReturn(false);
		when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
		doNothing().when(emailVerificationService).checkVerified(anyString());

		userService.join(joinRequest);

		verify(userRepository, times(1)).save(any(User.class));
		verify(emailVerificationService, times(1)).deleteVerification(joinRequest.getEmail());
	}

	@Test
	@DisplayName("중복된 사용자 아이디로 회원가입 시 예외 발생")
	void shouldThrowExceptionWhenUserIdExists() {
		JoinRequest joinRequest = new JoinRequest("existingUser", "홍길동", "test@example.com", "password123");

		when(userRepository.existsByUserId(anyString())).thenReturn(true);
		doNothing().when(emailVerificationService).checkVerified(anyString());

		assertThatThrownBy(() -> userService.join(joinRequest))
			.isInstanceOf(DuplicationException.class)
			.hasMessage("이미 존재하는 사용자 아이디입니다.");
	}

	@Test
	@DisplayName("중복된 이메일로 회원가입 시 예외 발생")
	void shouldThrowExceptionWhenEmailExists() {
		JoinRequest joinRequest = new JoinRequest("newUser", "홍길동", "existing@example.com", "password123");

		when(userRepository.existsByUserId(anyString())).thenReturn(false);
		when(userRepository.existsByEmail(anyString())).thenReturn(true);
		doNothing().when(emailVerificationService).checkVerified(anyString());

		assertThatThrownBy(() -> userService.join(joinRequest))
			.isInstanceOf(DuplicationException.class)
			.hasMessage("이미 존재하는 이메일입니다.");
	}

	@Test
	@DisplayName("회원가입 시 비밀번호가 인코딩되는지 확인")
	void shouldEncodePasswordDuringJoin() {
		JoinRequest joinRequest = new JoinRequest("newUser", "userName", "test@example.com", "password");

		when(userRepository.existsByUserId(anyString())).thenReturn(false);
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

		doThrow(new EmailVerifiedException())
			.when(emailVerificationService).checkVerified(joinRequest.getEmail());

		assertThatThrownBy(() -> userService.join(joinRequest))
			.isInstanceOf(EmailVerifiedException.class);
	}

}
