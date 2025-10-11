package com.project.triplog.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.triplog.domain.User;
import com.project.triplog.dto.trip.TripCreateRequest;
import com.project.triplog.global.response.ApiResponse;
import com.project.triplog.service.TripService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
public class TripController {
	private final TripService tripService;

	@PostMapping("/trips")
	public ApiResponse<Void> createTrip(@RequestBody @Valid TripCreateRequest tripCreateRequest,
		@AuthenticationPrincipal(expression = "user") User user) {
		tripService.createTrip(tripCreateRequest, user);
		return ApiResponse.success("여행이 성공적으로 생성되었습니다");
	}
}
