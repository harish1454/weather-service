package com.example.demo;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller that exposes weather data endpoints.
 */
@RestController
public class MyWeatherController {

    private final MyWeatherService myWeatherService;

    /**
     * Constructs a new {@code MyWeatherController} with the required dependencies.
     *
     * @param myWeatherService the service used to retrieve weather data
     */
    public MyWeatherController(MyWeatherService myWeatherService) {
        this.myWeatherService = myWeatherService;
    }

    /**
     * Retrieves current weather data for the specified city.
     *
     * @param city the city name to look up
     * @return a ResponseEntity containing the weather data as JSON
     */
    @GetMapping("/weather")
    public ResponseEntity<String> getWeather(@RequestParam("city") String city) {
        return myWeatherService.getWeather(city);
    }
}
