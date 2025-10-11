package com.project.triplog.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.triplog.domain.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	boolean existsByUserId(String userId);

	boolean existsByEmail(String email);

	Optional<User> findByUserId(String userId);
}
