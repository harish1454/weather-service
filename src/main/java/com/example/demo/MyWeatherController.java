package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
//@CrossOrigin
public class MyWeatherController {
	
		@Autowired
		private MyWeatherService myWeatherService;
		
		@RequestMapping(name="/weather")
		public ResponseEntity<String> getWeather(@RequestParam("city") String city) {
			
			return myWeatherService.getWeather(city);
			
		}
}
