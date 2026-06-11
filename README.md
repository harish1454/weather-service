# Weather Service

A Spring Boot REST API that provides current weather information for any city using the OpenWeatherMap API.

## Tech Stack

- **Java 8**
- **Spring Boot 2.1.2**
- **Spring Web** (REST controller)
- **Spring Boot Actuator** (health/metrics endpoints)
- **OpenWeatherMap API** (weather data provider)

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

```
GET /?city={cityName}
```

**Parameters:**

| Name   | Type   | Description                    |
|--------|--------|--------------------------------|
| `city` | String | Name of the city (e.g., London)|

**Example Request:**

```bash
curl "http://localhost:8080/?city=London"
```

**Example Response:**

Returns weather data in JSON format from the OpenWeatherMap API, including temperature (in metric units), humidity, wind speed, and weather conditions.

## Project Structure

```
src/main/java/com/example/demo/
├── MyWeatherApplication.java   # Spring Boot application entry point
├── MyWeatherController.java    # REST controller exposing the weather endpoint
└── MyWeatherService.java       # Service layer that calls OpenWeatherMap API
```

## Actuator Endpoints

Spring Boot Actuator is included, providing endpoints such as:

- `GET /actuator/health` - Application health status
- `GET /actuator/info` - Application info

## License

This project is for demonstration purposes.
