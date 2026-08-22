package com.example.vehicle_tracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class VehicleTrackerApplication {

	public static void main(String[] args) {
		SpringApplication.run(VehicleTrackerApplication.class, args);
	}

}
