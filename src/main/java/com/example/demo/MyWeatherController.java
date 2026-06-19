package com.example.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;

@RestController
public class MyWeatherController {

	private static final Logger logger = LoggerFactory.getLogger(MyWeatherController.class);

	@Autowired
	private MyWeatherService myWeatherService;

	@GetMapping("/weather")
	public ResponseEntity<String> getWeather(@RequestParam("city") String city) {
		logger.info("Received weather request for city: {}", city);

		if (city == null || city.trim().isEmpty()) {
			return ResponseEntity.badRequest().body("{\"error\": \"City parameter must not be empty\"}");
		}

		try {
			ResponseEntity<String> response = myWeatherService.getWeather(city);
			logger.info("Successfully retrieved weather data for city: {}", city);
			return response;
		} catch (HttpClientErrorException e) {
			logger.warn("Client error fetching weather for city '{}': {}", city, e.getMessage());
			return ResponseEntity.status(e.getStatusCode())
					.body("{\"error\": \"" + e.getStatusText() + "\"}");
		} catch (ResourceAccessException e) {
			logger.error("Unable to reach weather API for city '{}': {}", city, e.getMessage());
			return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
					.body("{\"error\": \"Weather service is currently unavailable\"}");
		} catch (Exception e) {
			logger.error("Unexpected error fetching weather for city '{}': {}", city, e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("{\"error\": \"An unexpected error occurred\"}");
		}
	}
}
