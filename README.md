# ☀️ Weather Service

[![Java](https://img.shields.io/badge/Java-17-orange?logo=openjdk&logoColor=white)](https://openjdk.org/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.1-brightgreen?logo=springboot&logoColor=white)](https://spring.io/projects/spring-boot)
[![Maven](https://img.shields.io/badge/Maven-3.x-C71A36?logo=apachemaven&logoColor=white)](https://maven.apache.org/)
[![License](https://img.shields.io/badge/License-Demo-blue)](#license)

A lightweight Spring Boot application that provides real-time weather information for any city worldwide using the [OpenWeatherMap API](https://openweathermap.org/api). Simply query the REST endpoint with a city name and receive current weather data including temperature, humidity, and atmospheric conditions.

---

## 📑 Table of Contents

- [Features](#-features)
- [Tech Stack](#-tech-stack)
- [Prerequisites](#-prerequisites)
- [Getting Started](#-getting-started)
- [Configuration](#-configuration)
- [API Usage](#-api-usage)
- [Docker](#-docker)
- [Project Structure](#-project-structure)
- [Running Tests](#-running-tests)
- [Troubleshooting](#-troubleshooting)
- [Contributing](#-contributing)
- [Acknowledgements](#-acknowledgements)
- [License](#-license)

---

## ✨ Features

- **REST API** endpoint to fetch current weather data by city name
- **Metric Units** - returns temperature in Celsius
- **Static Welcome Page** served at the root URL
- **Health Monitoring** via Spring Boot Actuator
- **Lightweight** - minimal dependencies, fast startup

---

## 🛠️ Tech Stack

| Technology | Version | Purpose |
|------------|---------|---------|
| Java | 17 | Runtime |
| Spring Boot | 3.4.1 | Application framework |
| Spring Web | - | REST endpoints |
| Spring Boot Actuator | - | Health monitoring |
| Maven | 3.x | Build & dependency management |
| OpenWeatherMap API | 2.5 | Weather data provider |

---

## 📋 Prerequisites

Before you begin, ensure you have the following installed:

- **Java 17** (JDK 17) or higher - [Download](https://adoptium.net/)
- **Maven 3.x** - [Download](https://maven.apache.org/download.cgi)
- **OpenWeatherMap API Key** - [Get one free](https://openweathermap.org/appid)

---

## 🚀 Getting Started

### 1. Clone the Repository

```bash
git clone https://github.com/harish1454/weather-service.git
cd weather-service
```

### 2. Build the Project

```bash
mvn clean install
```

### 3. Run the Application

```bash
mvn spring-boot:run
```

The application starts on the default port `8080`.

### 4. Access the Application

| Endpoint | URL |
|----------|-----|
| Welcome Page | [http://localhost:8080/](http://localhost:8080/) |
| Weather API | [http://localhost:8080/weather?city=London](http://localhost:8080/weather?city=London) |
| Health Check | [http://localhost:8080/actuator/health](http://localhost:8080/actuator/health) |

---

## ⚙️ Configuration

### API Key Setup

The OpenWeatherMap API key is currently hardcoded in `MyWeatherService.java`. For production use, it is recommended to externalize this configuration using environment variables or application properties.

**Recommended approach using `application.properties`:**

```properties
# src/main/resources/application.properties
weather.api.key=${OPENWEATHER_API_KEY:your-default-key-here}
```

**Setting the environment variable:**

```bash
# Linux/macOS
export OPENWEATHER_API_KEY=your_api_key_here

# Windows (Command Prompt)
set OPENWEATHER_API_KEY=your_api_key_here

# Windows (PowerShell)
$env:OPENWEATHER_API_KEY="your_api_key_here"
```

> **Note:** Never commit API keys to version control. Consider using a `.env` file (added to `.gitignore`) or a secrets manager for production deployments.

### Application Properties

| Property | Default | Description |
|----------|---------|-------------|
| `server.port` | `8080` | Server port |
| `weather.api.key` | (hardcoded) | OpenWeatherMap API key |

---

## 🐳 Docker

### Building the Docker Image

You can containerize the application using the following `Dockerfile`:

```dockerfile
FROM eclipse-temurin:17-jdk-alpine AS build
WORKDIR /app
COPY . .
RUN ./mvnw clean package -DskipTests

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

### Run with Docker

```bash
# Build the image
docker build -t weather-service .

# Run the container
docker run -p 8080:8080 weather-service

# Run with environment variable for API key (recommended)
docker run -p 8080:8080 -e OPENWEATHER_API_KEY=your_key_here weather-service
```

### Docker Compose (Optional)

```yaml
version: '3.8'
services:
  weather-service:
    build: .
    ports:
      - "8080:8080"
    environment:
      - OPENWEATHER_API_KEY=${OPENWEATHER_API_KEY}
```

---

## 📡 API Usage

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

**More Examples:**

```bash
# Get weather for New York
curl "http://localhost:8080/weather?city=New%20York"

# Get weather for Tokyo
curl "http://localhost:8080/weather?city=Tokyo"

# Get weather for Mumbai
curl "http://localhost:8080/weather?city=Mumbai"
```

---

## 📁 Project Structure

```
weather-service/
├── .mvn/                           # Maven wrapper configuration
├── src/
│   ├── main/
│   │   ├── java/com/example/demo/
│   │   │   ├── MyWeatherApplication.java   # Main Spring Boot application class
│   │   │   ├── MyWeatherController.java    # REST controller handling /weather endpoint
│   │   │   └── MyWeatherService.java       # Service layer (calls OpenWeatherMap API)
│   │   └── resources/
│   │       ├── application.properties      # Application configuration
│   │       └── static/
│   │           └── index.html              # Welcome page
│   └── test/
│       └── java/com/example/demo/
│           └── MyWeatherApplicationTests.java  # Application tests
├── mvnw                            # Maven wrapper (Linux/macOS)
├── mvnw.cmd                        # Maven wrapper (Windows)
├── pom.xml                         # Maven project configuration
└── README.md                       # This file
```

---

## 🧪 Running Tests

```bash
# Run all tests
mvn test

# Run tests with verbose output
mvn test -X

# Run a specific test class
mvn test -Dtest=MyWeatherApplicationTests
```

---

## ❓ Troubleshooting

### Common Issues

<details>
<summary><strong>Port 8080 is already in use</strong></summary>

If port 8080 is occupied, you can change it by adding the following to `src/main/resources/application.properties`:

```properties
server.port=9090
```

Or pass it as a command-line argument:

```bash
mvn spring-boot:run -Dspring-boot.run.arguments=--server.port=9090
```
</details>

<details>
<summary><strong>401 Unauthorized from OpenWeatherMap</strong></summary>

This means the API key is invalid or has not been activated yet. After creating a new API key on OpenWeatherMap, it may take a few hours to become active. Verify your key at:

```
http://api.openweathermap.org/data/2.5/weather?q=London&APPID=YOUR_KEY
```
</details>

<details>
<summary><strong>City not found (404)</strong></summary>

Ensure the city name is spelled correctly. For cities with spaces, use URL encoding:

```bash
curl "http://localhost:8080/weather?city=New%20York"
```
</details>

<details>
<summary><strong>Connection timeout errors</strong></summary>

This typically indicates a network issue. Verify that:
- Your machine has internet access
- The OpenWeatherMap API (api.openweathermap.org) is reachable
- No firewall is blocking outbound HTTP requests
</details>

### FAQ

**Q: How do I change the unit system from Celsius to Fahrenheit?**
A: Modify the `units` query parameter in `MyWeatherService.java` from `"metric"` to `"imperial"`.

**Q: Is there a rate limit on the API?**
A: The free tier of OpenWeatherMap allows up to 1,000 API calls per day and 60 calls per minute.

**Q: Can I deploy this to a cloud provider?**
A: Yes! This application can be deployed to any platform that supports Java 17, including AWS, Azure, GCP, Heroku, and Railway.

---

## 🤝 Contributing

Contributions are welcome! Here is how you can help:

### How to Contribute

1. **Fork** the repository
2. **Create** a feature branch (`git checkout -b feature/amazing-feature`)
3. **Commit** your changes (`git commit -m 'feat: add amazing feature'`)
4. **Push** to the branch (`git push origin feature/amazing-feature`)
5. **Open** a Pull Request

### Guidelines

- Follow existing code style and naming conventions
- Write meaningful commit messages using [Conventional Commits](https://www.conventionalcommits.org/)
- Add tests for new features where applicable
- Update documentation for any user-facing changes
- Keep pull requests focused on a single concern

### Reporting Issues

Found a bug or have a suggestion? Please [open an issue](https://github.com/harish1454/weather-service/issues) with:
- A clear description of the problem or enhancement
- Steps to reproduce (for bugs)
- Expected vs actual behavior

---

## 🙏 Acknowledgements

- [OpenWeatherMap](https://openweathermap.org/) for providing the free weather data API
- [Spring Boot](https://spring.io/projects/spring-boot) for the excellent application framework
- [Spring Initializr](https://start.spring.io/) for project scaffolding
- All contributors who help improve this project

---

## 📄 License

This project is for demonstration and educational purposes. Feel free to use, modify, and distribute as needed.

---

<p align="center">
  Made with ❤️ using Spring Boot
</p>
