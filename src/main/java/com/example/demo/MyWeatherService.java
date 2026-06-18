package com.example.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * Service responsible for retrieving weather data from the OpenWeatherMap API.
 */
@Service
public class MyWeatherService {

    private static final Logger logger = LoggerFactory.getLogger(MyWeatherService.class);

    private final RestTemplate restTemplate;
    private final String apiKey;
    private final String baseUrl;

    /**
     * Constructs a new {@code MyWeatherService} with the required dependencies.
     *
     * @param restTemplate the REST client used to call the weather API
     * @param apiKey       the OpenWeatherMap API key
     * @param baseUrl      the base URL of the OpenWeatherMap API
     */
    public MyWeatherService(RestTemplate restTemplate,
                            @Value("${weather.api.key}") String apiKey,
                            @Value("${weather.api.base-url}") String baseUrl) {
        this.restTemplate = restTemplate;
        this.apiKey = apiKey;
        this.baseUrl = baseUrl;
    }

    /**
     * Retrieves current weather data for the specified city.
     *
     * @param city the name of the city to query
     * @return a ResponseEntity containing the weather data as JSON
     */
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
