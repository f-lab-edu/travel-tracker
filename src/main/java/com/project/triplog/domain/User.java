package com.project.triplog.domain;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.project.triplog.dto.user.JoinRequest;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String username;
	private String name;
	private String email;
	private String password;
	@CreatedDate
	private LocalDateTime createdAt;

	public static User from(JoinRequest joinRequest, String newPassword) {
		User user = new User();
		user.username = joinRequest.getUsername();
		user.name = joinRequest.getName();
		user.email = joinRequest.getEmail();
		user.password = newPassword;
		return user;
	}
}
