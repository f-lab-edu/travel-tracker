package com.project.triplog.security;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtTokenProvider {
	@Value("${jwt.secret-key}")
	private String secretKey;
	@Value("${jwt.expiration-time}")
	private long expirationTime;

	public String createToken(String userId) {
		Claims claims = Jwts.claims().setSubject(userId);
		Date now = new Date();
		Date expiry = new Date(now.getTime() + expirationTime);

		return Jwts.builder()
			.setClaims(claims)
			.setIssuedAt(now)
			.setExpiration(expiry)
			.signWith(SignatureAlgorithm.HS256, secretKey)
			.compact();
	}

	public String getUserId(String token) {
		return Jwts.parser().setSigningKey(secretKey)
			.parseClaimsJws(token)
			.getBody()
			.getSubject();
	}

	public boolean validateToken(String token) {
		try {
			Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
			return true;
		} catch (JwtException | IllegalArgumentException e) {
			return false;
		}
	}
}
