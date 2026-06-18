package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/**
 * Main entry point for the Weather Service application.
 *
 * <p>Bootstraps the Spring Boot application and provides shared bean definitions.
 */
@SpringBootApplication
public class MyWeatherApplication {

    /**
     * Starts the Weather Service application.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(MyWeatherApplication.class, args);
    }

    /**
     * Creates a configured {@link RestTemplate} bean for making HTTP requests.
     *
     * @param builder the Spring-provided RestTemplateBuilder
     * @return a configured RestTemplate instance
     */
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

}
