package com.project.triplog.service;

import com.project.triplog.domain.EmailVerification;
import com.project.triplog.dto.EmailRequest;
import com.project.triplog.exception.EmailSendException;
import com.project.triplog.exception.EmailVerifiedException;
import com.project.triplog.repository.EmailVerificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailVerificationService {
    private final EmailVerificationRepository emailVerificationRepository;
    private final JavaMailSender mailSender;

    public String generateToken(String email) {
        String token = UUID.randomUUID().toString();
        System.out.println("Generated token for email " + email + ": " + token);
        EmailVerification emailVerification = EmailVerification.of(email, token);
        emailVerificationRepository.save(emailVerification);
        return token;
    }

    public boolean verifyToken(String email, String token) {
        Optional<EmailVerification> emailVerificationOpt = emailVerificationRepository.findByEmail(email);
        if (emailVerificationOpt.isEmpty()) {
            throw new EmailVerifiedException();
        }

        EmailVerification emailVerification = emailVerificationOpt.get();
        if (!emailVerification.getToken().equals(token)) {
            throw new EmailVerifiedException();
        }
        emailVerification.completeVerification();
        return true;
    }

    public void sendVerificationEmail(EmailRequest emailRequest) {
        String token = generateToken(emailRequest.getEmail());
        String verificationUrl = "http://localhost:8080/api/v1/members/email-verification?email="
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
            throw new EmailSendException();
        }
    }

    public void checkVerified(String email) {
        EmailVerification emailVerification = emailVerificationRepository.findByEmail(email)
                .orElseThrow(() -> new EmailVerifiedException());
        if (!emailVerification.isVerified()) {
            throw new EmailVerifiedException();
        }
    }

    public void deleteVerification(String email) {
        emailVerificationRepository.deleteByEmail(email);
    }
}
