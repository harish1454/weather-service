package com.example.demo;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MyWeatherController {

    private final MyWeatherService myWeatherService;

    public MyWeatherController(MyWeatherService myWeatherService) {
        this.myWeatherService = myWeatherService;
    }

    @GetMapping("/weather")
    public ResponseEntity<String> getWeather(@RequestParam("city") String city) {
        return myWeatherService.getWeather(city);
    }
}
