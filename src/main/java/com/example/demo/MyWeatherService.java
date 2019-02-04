package com.example.demo;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class MyWeatherService {
	
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private RestTemplate restTemplate;
	
	
	public ResponseEntity<String> getWeather(String city) {
		UriComponentsBuilder builder = UriComponentsBuilder
			    .fromUriString("http://api.openweathermap.org/data/2.5/weather")
			    .queryParam("q", city)
			    .queryParam("APPID", "d5b5488bbfe859639c0b208f29538344").
			    queryParam("units", "metric");
		
		logger.info("URL "+ builder.toUriString());
		
		ResponseEntity<String> json = restTemplate.getForEntity
		(builder.build().encode().toUri(), String.class);
		return json;
	}

}
