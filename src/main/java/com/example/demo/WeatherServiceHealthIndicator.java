package com.example.demo;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * Custom health indicator that checks connectivity to the OpenWeatherMap API.
 */
@Component
public class WeatherServiceHealthIndicator implements HealthIndicator {

    private final RestTemplate restTemplate;

    public WeatherServiceHealthIndicator(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public Health health() {
        try {
            restTemplate.headForHeaders("http://api.openweathermap.org");
            return Health.up()
                    .withDetail("openWeatherMapApi", "reachable")
                    .build();
        } catch (Exception e) {
            return Health.down()
                    .withDetail("openWeatherMapApi", "unreachable")
                    .withDetail("error", e.getMessage())
                    .build();
        }
    }
}
