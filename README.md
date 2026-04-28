# Weather Service

A Spring Boot REST API that provides current weather data for any city by proxying requests to the [OpenWeatherMap API](https://openweathermap.org/api).

## Tech Stack

- **Java 8**
- **Spring Boot 2.1.2**
- **Spring Web** - REST controller and `RestTemplate` for HTTP calls
- **Spring Boot Actuator** - health checks and application metrics
- **Maven** - build and dependency management (Maven Wrapper included)

## Prerequisites

- **Java 8** (JDK 1.8) or later
- No local Maven installation required (the included Maven Wrapper `mvnw` handles it)

## Build

```bash
# Build the project and run tests
./mvnw clean package

# Build without running tests
./mvnw clean package -DskipTests
```

The packaged JAR will be created at `target/myWeather-0.0.1-SNAPSHOT.jar`.

## Run

```bash
# Using the Maven Spring Boot plugin
./mvnw spring-boot:run

# Or run the packaged JAR directly
java -jar target/myWeather-0.0.1-SNAPSHOT.jar
```

The application starts on the default port **8080**.

## API Endpoints

### Get Current Weather

Retrieves current weather data for a given city. Returns temperature in metric units (Celsius).

**Request:**

```
GET /?city={cityName}
```

| Parameter | Type   | Required | Description                      |
|-----------|--------|----------|----------------------------------|
| `city`    | string | Yes      | Name of the city to look up      |

**Example:**

```bash
curl "http://localhost:8080/?city=London"
```

**Response (200 OK):**

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
    "humidity": 81,
    "temp_min": 11.0,
    "temp_max": 14.0
  },
  "wind": { "speed": 4.1, "deg": 80 },
  "name": "London"
}
```

> **Note:** The response is the raw JSON payload from the OpenWeatherMap API. See the [OpenWeatherMap Current Weather documentation](https://openweathermap.org/current) for the full response schema.

### Actuator Endpoints

Spring Boot Actuator is included, exposing health and info endpoints:

```bash
# Application health check
curl http://localhost:8080/actuator/health

# Available actuator endpoints
curl http://localhost:8080/actuator
```

## Configuration

The application uses the [OpenWeatherMap API](https://openweathermap.org/api) to fetch weather data. The API key is currently configured in `MyWeatherService.java`.

To use your own API key, update the `APPID` query parameter in `src/main/java/com/example/demo/MyWeatherService.java`:

```java
.queryParam("APPID", "your-api-key-here")
```

You can obtain a free API key by signing up at [OpenWeatherMap](https://openweathermap.org/appid).

## Project Structure

```
weather-service/
├── pom.xml                          # Maven project configuration
├── mvnw / mvnw.cmd                  # Maven Wrapper scripts
├── src/
│   ├── main/
│   │   ├── java/com/example/demo/
│   │   │   ├── MyWeatherApplication.java   # Spring Boot entry point
│   │   │   ├── MyWeatherController.java    # REST controller (GET /?city=...)
│   │   │   └── MyWeatherService.java       # Service layer (calls OpenWeatherMap)
│   │   └── resources/
│   │       └── application.properties      # Application configuration
│   └── test/
│       └── java/com/example/demo/
│           └── MyWeatherApplicationTests.java  # Spring Boot context test
└── README.md
```

## Running Tests

```bash
./mvnw test
```
