package com.project.triplog.service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.UUID;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.project.triplog.domain.EmailVerification;
import com.project.triplog.dto.user.EmailRequest;
import com.project.triplog.global.exception.ApiException;
import com.project.triplog.global.exception.ErrorCode;
import com.project.triplog.repository.EmailVerificationRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailVerificationService {
	private final EmailVerificationRepository emailVerificationRepository;
	private final JavaMailSender mailSender;

	public String generateToken(String email) {
		String token = UUID.randomUUID().toString();
		EmailVerification emailVerification = EmailVerification.of(email, token);
		emailVerificationRepository.save(emailVerification);
		return token;
	}

	public boolean verifyToken(String email, String token) {
		Optional<EmailVerification> emailVerificationOpt = emailVerificationRepository.findByEmail(email);
		if (emailVerificationOpt.isEmpty()) {
			throw new ApiException(ErrorCode.EMAIL_NOT_VERIFIED);
		}

		EmailVerification emailVerification = emailVerificationOpt.get();
		if (!emailVerification.getToken().equals(token)) {
			throw new ApiException(ErrorCode.EMAIL_NOT_VERIFIED);
		}
		emailVerification.completeVerification();
		return true;
	}

	public void sendVerificationEmail(EmailRequest emailRequest) {
		String token = generateToken(emailRequest.getEmail());
		String verificationUrl = "http://localhost:8080/users/email/verification?email="
			+ URLEncoder.encode(emailRequest.getEmail(), StandardCharsets.UTF_8)
			+ "&token=" + token;
		String subject = "회원가입 : 이메일 인증";
		String body = "아래 링크를 클릭해 이메일 인증을 완료해 주세요:\n" + verificationUrl;

		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(emailRequest.getEmail());
		message.setSubject(subject);
		message.setText(body);

		try {
			mailSender.send(message);
		} catch (Exception e) {
			log.error("Failed to send verification email to {}", emailRequest.getEmail());
			throw new ApiException(ErrorCode.EMAIL_SEND_FAILURE);
		}
	}

	public void checkVerified(String email) {
		EmailVerification emailVerification = emailVerificationRepository.findByEmail(email)
			.orElseThrow(() -> new ApiException(ErrorCode.EMAIL_NOT_VERIFIED));
		if (!emailVerification.isVerified()) {
			throw new ApiException(ErrorCode.EMAIL_NOT_VERIFIED);
		}
	}

	public void deleteVerification(String email) {
		emailVerificationRepository.deleteByEmail(email);
	}
}
