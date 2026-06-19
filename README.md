# Weather Service

A Spring Boot application that provides weather information for a given city using the [OpenWeatherMap API](https://openweathermap.org/api).

## Prerequisites

- Java 8 or higher
- Maven 3.x (or use the included Maven wrapper)

## Getting Started

### Clone the repository

```bash
git clone https://github.com/harish1454/weather-service.git
cd weather-service
```

### Build the project

```bash
./mvnw clean package
```

### Run the application

```bash
./mvnw spring-boot:run
```

The application starts on the default port `8080`.

## API Endpoints

### Get Weather

Retrieves current weather data for a specified city.

```
GET /?city={cityName}
```

**Parameters:**

| Parameter | Type   | Description                      |
|-----------|--------|----------------------------------|
| `city`    | String | Name of the city to query weather for |

**Example Request:**

```bash
curl "http://localhost:8080/?city=London"
```

**Example Response:**

Returns weather data in JSON format from the OpenWeatherMap API, including temperature (in metric units), humidity, wind speed, and more.

### Welcome Page

A static welcome page is served at the root path:

```
GET /
```

### Actuator

Spring Boot Actuator endpoints are available for monitoring and health checks:

```
GET /actuator/health
GET /actuator/info
```

## Technology Stack

- **Framework:** Spring Boot 2.1.2
- **Language:** Java 8
- **Build Tool:** Maven
- **External API:** OpenWeatherMap API
- **Monitoring:** Spring Boot Actuator

## Project Structure

```
src/main/java/com/example/demo/
  MyWeatherApplication.java   - Application entry point and configuration
  MyWeatherController.java    - REST controller handling weather requests
  MyWeatherService.java       - Service layer calling OpenWeatherMap API

src/main/resources/
  application.properties      - Application configuration
  static/index.html           - Welcome page
```

## License

This project is for demonstration purposes.
