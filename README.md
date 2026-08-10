# Weather Service

A Spring Boot application that provides real-time weather information for any city using the [OpenWeatherMap API](https://openweathermap.org/api).

## Features

- REST API endpoint to fetch current weather data by city name
- Returns temperature in metric units (Celsius)
- Static welcome page served at the root URL
- Spring Boot Actuator for health monitoring

## Tech Stack

- **Java** 1.8
- **Spring Boot** 2.1.2
- **Spring Web** for REST endpoints
- **Spring Boot Actuator** for application monitoring
- **Maven** for build and dependency management

## Prerequisites

- Java 8 (JDK 1.8) or higher
- Maven 3.x
- An OpenWeatherMap API key (currently configured in the service)

## Getting Started

### Build

```bash
mvn clean install
```

### Run

```bash
mvn spring-boot:run
```

The application starts on the default port `8080`.

### Access the Application

- **Welcome Page:** [http://localhost:8080/](http://localhost:8080/)
- **Weather API:** [http://localhost:8080/weather?city=London](http://localhost:8080/weather?city=London)

## API Usage

### Get Weather by City

**Endpoint:** `GET /weather`

**Query Parameters:**

| Parameter | Type   | Required | Description              |
|-----------|--------|----------|--------------------------|
| `city`    | String | Yes      | Name of the city         |

**Example Request:**

```bash
curl "http://localhost:8080/weather?city=London"
```

**Example Response:**

```json
{
  "coord": { "lon": -0.13, "lat": 51.51 },
  "weather": [{ "id": 300, "main": "Drizzle", "description": "light intensity drizzle" }],
  "main": { "temp": 12.5, "pressure": 1012, "humidity": 81 },
  "name": "London"
}
```

## Project Structure

```
weather-service/
├── pom.xml
├── src/
│   ├── main/
│   │   ├── java/com/example/demo/
│   │   │   ├── MyWeatherApplication.java   # Main Spring Boot application
│   │   │   ├── MyWeatherController.java    # REST controller
│   │   │   └── MyWeatherService.java       # Weather service (calls OpenWeatherMap)
│   │   └── resources/
│   │       └── static/
│   │           └── index.html              # Welcome page
│   └── test/
│       └── java/com/example/demo/
│           └── MyWeatherApplicationTests.java
└── README.md
```

## Running Tests

```bash
mvn test
```

## License

This project is for demonstration purposes.
<!-- Load test PR #17 - Mon Aug 10 07:35:03 PM UTC 2026 -->
