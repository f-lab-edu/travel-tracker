package com.project.triplog.dto.trip;

import java.time.LocalDate;
import java.util.List;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class TripCreateRequest {
	private final String title;
	private final String description;
	private final LocalDate startDate;
	private final LocalDate endDate;
	private final List<String> countryCodes;
}
