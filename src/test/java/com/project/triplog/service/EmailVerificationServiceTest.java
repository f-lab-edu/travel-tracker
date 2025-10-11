package com.project.triplog.service;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import com.project.triplog.domain.EmailVerification;
import com.project.triplog.dto.EmailRequest;
import com.project.triplog.global.exception.ApiException;
import com.project.triplog.repository.EmailVerificationRepository;

class EmailVerificationServiceTest {
	private EmailVerificationRepository emailVerificationRepository;
	private JavaMailSender mailSender;
	private EmailVerificationService emailVerificationService;

	@BeforeEach
	void setUp() {
		emailVerificationRepository = mock(EmailVerificationRepository.class);
		mailSender = mock(JavaMailSender.class);
		emailVerificationService = new EmailVerificationService(emailVerificationRepository, mailSender);
	}

	@Test
	@DisplayName("이메일에 대해 인증 토큰을 생성하고 저장한다")
	void shouldGenerateAndSaveToken() {
		String email = "test@example.com";

		String token = emailVerificationService.generateToken(email);

		assertThat(token).isNotBlank();
		verify(emailVerificationRepository).save(any(EmailVerification.class));
	}

	@Test
	@DisplayName("토큰이 유효하면 true 반환하고 인증 상태로 변경한다")
	void shouldVerifyTokenSuccessfully() {
		String email = "test@example.com";
		String token = UUID.randomUUID().toString();
		EmailVerification verification = EmailVerification.of(email, token);

		when(emailVerificationRepository.findByEmail(email))
			.thenReturn(Optional.of(verification));

		boolean result = emailVerificationService.verifyToken(email, token);

		assertThat(result).isTrue();
		assertThat(verification.isVerified()).isTrue();
	}

	@Test
	@DisplayName("이메일에 대한 인증 정보가 없으면 예외 발생")
	void shouldThrowIfVerificationNotFound() {
		String email = "notfound@example.com";
		String token = UUID.randomUUID().toString();

		when(emailVerificationRepository.findByEmail(email)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> emailVerificationService.verifyToken(email, token))
			.isInstanceOf(ApiException.class);
	}

	@Test
	@DisplayName("토큰이 일치하지 않으면 예외 발생")
	void shouldThrowIfTokenMismatch() {
		String email = "test@example.com";
		String correctToken = UUID.randomUUID().toString();
		String wrongToken = UUID.randomUUID().toString();

		EmailVerification verification = EmailVerification.of(email, correctToken);
		when(emailVerificationRepository.findByEmail(email)).thenReturn(Optional.of(verification));

		assertThatThrownBy(() -> emailVerificationService.verifyToken(email, wrongToken))
			.isInstanceOf(ApiException.class);
	}

	@Test
	@DisplayName("이메일 인증 메일을 정상적으로 발송한다")
	void shouldSendVerificationEmailSuccessfully() {
		EmailRequest request = new EmailRequest("test@example.com");

		emailVerificationService.sendVerificationEmail(request);

		verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
		verify(emailVerificationRepository, times(1)).save(any(EmailVerification.class));
	}

	@Test
	@DisplayName("이메일 전송에 실패하면 예외 발생")
	void shouldThrowWhenEmailSendFails() {
		EmailRequest request = new EmailRequest("fail@example.com");

		doThrow(new RuntimeException("SMTP error"))
			.when(mailSender).send(any(SimpleMailMessage.class));

		assertThatThrownBy(() -> emailVerificationService.sendVerificationEmail(request))
			.isInstanceOf(ApiException.class);
	}

	@Test
	@DisplayName("인증이 완료되지 않았거나 정보가 없으면 예외 발생")
	void shouldThrowIfNotVerified() {
		String email = "unverified@example.com";
		EmailVerification verification = EmailVerification.of(email, UUID.randomUUID().toString());

		when(emailVerificationRepository.findByEmail(email)).thenReturn(Optional.of(verification));

		assertThatThrownBy(() -> emailVerificationService.checkVerified(email))
			.isInstanceOf(ApiException.class);
	}

	@Test
	@DisplayName("이메일 인증 정보를 삭제한다")
	void shouldDeleteVerificationSuccessfully() {
		String email = "test@example.com";

		emailVerificationService.deleteVerification(email);

		verify(emailVerificationRepository).deleteByEmail(email);
	}

}
