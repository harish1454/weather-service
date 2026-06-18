package com.example.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class MyWeatherService {

    private static final Logger logger = LoggerFactory.getLogger(MyWeatherService.class);

    private final RestTemplate restTemplate;

    @Value("${weather.api.key}")
    private String apiKey;

    @Value("${weather.api.base-url}")
    private String baseUrl;

    public MyWeatherService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ResponseEntity<String> getWeather(String city) {
        UriComponentsBuilder builder = UriComponentsBuilder
                .fromUriString(baseUrl + "/weather")
                .queryParam("q", city)
                .queryParam("APPID", apiKey)
                .queryParam("units", "metric");

        logger.info("URL: {}", builder.toUriString());

        ResponseEntity<String> json = restTemplate.getForEntity(
                builder.build().encode().toUri(), String.class);
        return json;
    }

}
