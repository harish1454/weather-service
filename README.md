<div align="center">

# Weather Service

[![Java](https://img.shields.io/badge/Java-8-orange?style=for-the-badge&logo=java&logoColor=white)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-2.1.2-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)](https://spring.io/projects/spring-boot)
[![Maven](https://img.shields.io/badge/Maven-3.x-C71A36?style=for-the-badge&logo=apache-maven&logoColor=white)](https://maven.apache.org/)

A lightweight RESTful weather service that provides real-time weather data for any city worldwide, powered by the [OpenWeatherMap API](https://openweathermap.org/api).

---

</div>

## Features

- Real-time weather data retrieval by city name
- Metric units (Celsius) for temperature readings
- RESTful API with JSON responses
- Spring Boot Actuator for health monitoring and metrics
- CORS-ready architecture

---

## Tech Stack

| Technology | Version | Purpose |
|:-----------|:--------|:--------|
| Java | 8 | Runtime |
| Spring Boot | 2.1.2.RELEASE | Application framework |
| Spring Web | 5.x | REST controller & HTTP client |
| Spring Actuator | 2.1.x | Health checks & monitoring |
| Maven | 3.x | Build & dependency management |
| OpenWeatherMap API | 2.5 | Weather data provider |

---

## Prerequisites

Before running this project, ensure you have the following installed:

- **Java 8** (JDK 1.8+)
- **Maven 3.x** (or use the included Maven Wrapper)
- An **OpenWeatherMap API key** ([get one free here](https://openweathermap.org/appid))

---

## Getting Started

### Clone the repository

```bash
git clone https://github.com/harish1454/weather-service.git
cd weather-service
```

### Build the project

```bash
# Using Maven Wrapper (no Maven installation required)
./mvnw clean install

# Or using system Maven
mvn clean install
```

### Run the application

```bash
# Using Maven Wrapper
./mvnw spring-boot:run

# Or using the built JAR
java -jar target/myWeather-0.0.1-SNAPSHOT.jar
```

The service will start on **`http://localhost:8080`**.

---

## API Usage

### Get Weather by City

| Property | Value |
|:---------|:------|
| **Endpoint** | `/weather` |
| **Method** | `GET` |
| **Parameter** | `city` (required) - Name of the city |

### Example Request

```bash
curl "http://localhost:8080/weather?city=London"
```

### Example Response

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
    "temp": 14.5,
    "pressure": 1012,
    "humidity": 81,
    "temp_min": 13.0,
    "temp_max": 16.0
  },
  "wind": { "speed": 4.1, "deg": 80 },
  "name": "London"
}
```

> **Note:** Temperature values are returned in **Celsius** (metric units).

---

## Project Structure

```
weather-service/
├── src/
│   ├── main/
│   │   ├── java/com/example/demo/
│   │   │   ├── MyWeatherApplication.java    # Main application entry point
│   │   │   ├── MyWeatherController.java     # REST controller
│   │   │   └── MyWeatherService.java        # Weather data service
│   │   └── resources/
│   │       └── application.properties       # App configuration
│   └── test/
│       └── java/com/example/demo/
│           └── MyWeatherApplicationTests.java
├── pom.xml                                   # Maven configuration
├── mvnw                                      # Maven Wrapper (Unix)
├── mvnw.cmd                                  # Maven Wrapper (Windows)
└── README.md
```

---

## Actuator Endpoints

Spring Boot Actuator provides built-in monitoring endpoints:

| Endpoint | Description |
|:---------|:------------|
| `/actuator/health` | Application health status |
| `/actuator/info` | Application information |
| `/actuator/metrics` | Application metrics |

---

## Configuration

Application settings can be modified in `src/main/resources/application.properties`:

```properties
# Server port (default: 8080)
server.port=8080
```

---

<div align="center">

Made with Spring Boot

</div>
