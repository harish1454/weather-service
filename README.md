# Weather Service API

A Spring Boot REST API that provides real-time weather information for any city using the [OpenWeatherMap API](https://openweathermap.org/current).

## Overview

This application exposes a simple REST endpoint that accepts a city name and returns current weather data including temperature, humidity, wind speed, and general conditions. Temperature values are returned in metric units (Celsius).

## Tech Stack

- **Java 8**
- **Spring Boot 2.1.2**
- **Spring Web** - REST controller and RestTemplate for external API calls
- **Spring Boot Actuator** - Health checks and monitoring endpoints
- **Maven** - Build and dependency management

## Prerequisites

- Java 8 or higher
- Maven 3.x (or use the included Maven Wrapper)

## Getting Started

### Build the project

```bash
./mvnw clean install
```

### Run the application

```bash
./mvnw spring-boot:run
```

The application will start on the default port `8080`.

## API Usage

### Get Weather by City

**Endpoint:**

```
GET /?city={cityName}
```

**Parameters:**

| Parameter | Type   | Required | Description                  |
|-----------|--------|----------|------------------------------|
| city      | String | Yes      | Name of the city to look up  |

**Example Request:**

```bash
curl "http://localhost:8080/?city=London"
```

**Example Response:**

```json
{
  "coord": { "lon": -0.13, "lat": 51.51 },
  "weather": [
    {
      "id": 300,
      "main": "Drizzle",
      "description": "light intensity drizzle",
      "icon": "09d"
    }
  ],
  "main": {
    "temp": 12.5,
    "pressure": 1012,
    "humidity": 81
  },
  "wind": { "speed": 4.1, "deg": 80 },
  "name": "London"
}
```

> Note: The response above is a simplified example. The actual OpenWeatherMap response includes additional fields.

### Health Check (Actuator)

```bash
curl http://localhost:8080/actuator/health
```

## Project Structure

```
src/main/java/com/example/demo/
  MyWeatherApplication.java   - Spring Boot application entry point
  MyWeatherController.java    - REST controller handling weather requests
  MyWeatherService.java       - Service layer calling OpenWeatherMap API
src/main/resources/
  application.properties      - Empty (uses Spring Boot defaults)
  static/index.html           - Static welcome page
```

## Configuration

The application uses the OpenWeatherMap API to fetch weather data. Weather data is returned in metric units by default.

**Important:** The API key and base URL are currently hardcoded in `MyWeatherService.java`. For production use, these values should be externalized to `application.properties` or environment variables to avoid committing secrets to source control.

To use this application, you will need to obtain your own API key from [OpenWeatherMap](https://openweathermap.org/appid) and replace the hardcoded value in `MyWeatherService.java`.

## Running Tests

```bash
./mvnw test
```

## License

This project is for demonstration purposes.
