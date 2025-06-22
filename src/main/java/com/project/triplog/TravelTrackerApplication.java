package com.project.triplog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class TravelTrackerApplication {

	public static void main(String[] args) {
		SpringApplication.run(TravelTrackerApplication.class, args);
	}

}
