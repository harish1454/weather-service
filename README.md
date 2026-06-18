# Weather Service

A Spring Boot application that provides weather data via a REST API, powered by the OpenWeatherMap API.

## Features

- Retrieve current weather data by city name
- RESTful API with JSON responses
- Spring Boot Actuator for health monitoring

## Prerequisites

- Java 8+
- Maven 3.6+ (or use the included Maven wrapper)

## Quick Start

```bash
# Build the project
./mvnw clean install

# Run the application
./mvnw spring-boot:run
```

The service will be available at `http://localhost:8080`.

## API Usage

### Get Weather by City

```
GET /weather?city={cityName}
```

**Example:**
```bash
curl "http://localhost:8080/weather?city=London"
```

## Configuration

Configure the application via `src/main/resources/application.properties`:

| Property | Description | Default |
|----------|-------------|---------|
| `server.port` | Server port | `8080` |

## Project Structure

```
src/
├── main/
│   ├── java/com/example/demo/
│   │   ├── MyWeatherApplication.java    # Application entry point
│   │   ├── MyWeatherController.java     # REST controller
│   │   └── MyWeatherService.java        # Business logic
│   └── resources/
│       ├── static/index.html            # Welcome page
│       └── application.properties       # App configuration
└── test/
    └── java/com/example/demo/
        └── MyWeatherApplicationTests.java
```

## Contributing

Please read [CONTRIBUTING.md](CONTRIBUTING.md) for details on our development process, coding standards, and how to submit pull requests.

## Style Guide

See [STYLE_GUIDE.md](STYLE_GUIDE.md) for detailed coding conventions and best practices followed in this project.

## License

This project is for demonstration purposes.
