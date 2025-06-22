package com.project.triplog.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class EmailVerification {
    @Id
    private String email;
    private String token;
    private boolean verified;
    @CreatedDate
    private LocalDateTime createdAt;

    public static EmailVerification of(String email, String token) {
        EmailVerification emailVerification = new EmailVerification();
        emailVerification.email = email;
        emailVerification.token = token;
        emailVerification.verified = false;
        return emailVerification;
    }

    public void completeVerification() {
        this.verified = true;
    }
}
