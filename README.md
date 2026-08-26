# ☀️ Weather Service

A lightweight Spring Boot application that provides real-time weather information for any city using the [OpenWeatherMap API](https://openweathermap.org/api).

## ✨ Features

- **REST API** endpoint to fetch current weather data by city name
- **Metric units** — temperature returned in Celsius
- **Static welcome page** served at the root URL
- **Spring Boot Actuator** for health checks and monitoring
- **Simple architecture** — clean Controller → Service layering

## 🛠️ Tech Stack

| Technology | Version |
|------------|---------|
| Java | 17 |
| Spring Boot | 3.4.1 |
| Spring Web | REST endpoints |
| Spring Boot Actuator | Monitoring & health |
| Maven | Build & dependency management |

## 📋 Prerequisites

- **JDK 17** or higher
- **Maven 3.x** (or use the included Maven Wrapper `./mvnw`)
- An [OpenWeatherMap API key](https://openweathermap.org/appid)

## 🚀 Getting Started

### Clone the repository

```bash
git clone https://github.com/harish1454/weather-service.git
cd weather-service
```

### Build

```bash
./mvnw clean install
```

### Run

```bash
./mvnw spring-boot:run
```

The application starts on the default port **8080**.

### Access the Application

| Resource | URL |
|----------|-----|
| Welcome Page | [http://localhost:8080/](http://localhost:8080/) |
| Weather API | [http://localhost:8080/weather?city=London](http://localhost:8080/weather?city=London) |
| Health Check | [http://localhost:8080/actuator/health](http://localhost:8080/actuator/health) |

## 📡 API Reference

### Get Weather by City

```
GET /weather?city={cityName}
```

| Parameter | Type | Required | Description |
|-----------|--------|----------|--------------------------|
| `city` | String | Yes | Name of the city to look up |

#### Example Request

```bash
curl "http://localhost:8080/weather?city=London"
```

#### Example Response

```json
{
  "coord": { "lon": -0.13, "lat": 51.51 },
  "weather": [
    {
      "id": 300,
      "main": "Drizzle",
      "description": "light intensity drizzle"
    }
  ],
  "main": {
    "temp": 12.5,
    "pressure": 1012,
    "humidity": 81
  },
  "name": "London"
}
```

## 📁 Project Structure

```
weather-service/
├── pom.xml                          # Maven project configuration
├── mvnw / mvnw.cmd                  # Maven Wrapper scripts
├── src/
│   ├── main/
│   │   ├── java/com/example/demo/
│   │   │   ├── MyWeatherApplication.java   # Spring Boot entry point & RestTemplate bean
│   │   │   ├── MyWeatherController.java    # REST controller — /weather endpoint
│   │   │   └── MyWeatherService.java       # Service layer — calls OpenWeatherMap API
│   │   └── resources/
│   │       ├── application.properties      # Application configuration
│   │       └── static/
│   │           └── index.html              # Static welcome page
│   └── test/
│       └── java/com/example/demo/
│           └── MyWeatherApplicationTests.java  # Integration tests
└── README.md
```

## 🧪 Running Tests

```bash
./mvnw test
```

## ⚙️ Configuration

The application can be configured via `src/main/resources/application.properties`:

| Property | Description | Default |
|----------|-------------|---------|
| `server.port` | HTTP port | 8080 |

> **Note:** The OpenWeatherMap API key is currently hardcoded in `MyWeatherService.java`. For production use, consider externalizing it to `application.properties` or environment variables.

## 🔮 Future Improvements

- [ ] Externalize API key to configuration / environment variable
- [ ] Add error handling for invalid city names
- [ ] Add caching to reduce API calls
- [ ] Add Swagger/OpenAPI documentation
- [ ] Containerize with Docker

## 📄 License

This project is for demonstration purposes.
