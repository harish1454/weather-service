# Weather Service

![Java](https://img.shields.io/badge/Java-17-blue)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.1-brightgreen)
![Maven](https://img.shields.io/badge/Maven-3.x-orange)
![License](https://img.shields.io/badge/License-Demo-lightgrey)

A Spring Boot application that provides real-time weather information for any city using the [OpenWeatherMap API](https://openweathermap.org/api).

## Features

- REST API endpoint to fetch current weather data by city name
- Returns temperature in metric units (Celsius)
- Static welcome page served at the root URL
- Spring Boot Actuator for health monitoring

## Tech Stack

- **Java** 17
- **Spring Boot** 3.4.1
- **Spring Web** for REST endpoints
- **Spring Boot Actuator** for application monitoring
- **Maven** for build and dependency management

## Prerequisites

- Java 17 or higher
- Maven 3.x
- An OpenWeatherMap API key (see [Configuration](#configuration) below)

## Configuration

The application requires an API key from [OpenWeatherMap](https://openweathermap.org/api) to fetch weather data.

### Setting up the API Key

1. Sign up at [OpenWeatherMap](https://openweathermap.org/) and generate a free API key.
2. Configure the API key using one of the following methods:

**Option A: Environment Variable (recommended)**

```bash
export OPENWEATHERMAP_API_KEY=your_api_key_here
```

**Option B: Application Properties**

Add the key to `src/main/resources/application.properties`:

```properties
weather.api.key=your_api_key_here
```

> **Note:** Never commit your API key to version control. Consider using environment variables or a secrets manager for production deployments.

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

## Contributing

Contributions are welcome! To get started:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/your-feature`)
3. Commit your changes (`git commit -m 'feat: add your feature'`)
4. Push to the branch (`git push origin feature/your-feature`)
5. Open a Pull Request

Please make sure your code compiles and all tests pass before submitting a PR.

## License

This project is for demonstration purposes.
