# AI Guidelines for Weather Service

This document provides guidelines for AI assistants working with this codebase. Follow these conventions to produce consistent, secure, and maintainable contributions.

---

## Table of Contents

- [Project Overview](#project-overview)
- [Architecture](#architecture)
- [Project Structure](#project-structure)
- [Build and Run Commands](#build-and-run-commands)
- [API Reference](#api-reference)
- [Coding Conventions](#coding-conventions)
- [Security Guidelines](#security-guidelines)
- [Error Handling](#error-handling)
- [Logging](#logging)
- [Testing](#testing)
- [Configuration Management](#configuration-management)
- [Dependency Management](#dependency-management)
- [Performance Considerations](#performance-considerations)
- [Contribution Workflow](#contribution-workflow)
- [Common Pitfalls](#common-pitfalls)

---

## Project Overview

| Property | Value |
|----------|-------|
| **Type** | Java / Spring Boot web application |
| **Build Tool** | Maven (with Maven Wrapper — `mvnw`) |
| **Java Version** | 1.8 |
| **Spring Boot Version** | 2.1.2.RELEASE |
| **Group ID** | `com.harish` |
| **Artifact ID** | `myWeather` |
| **Purpose** | Weather service that proxies requests to OpenWeatherMap API |

---

## Architecture

The application follows a **layered architecture** pattern:

```
┌─────────────────────────────────────────────┐
│              Client (Browser/API)            │
└──────────────────────┬──────────────────────┘
                       │ HTTP
┌──────────────────────▼──────────────────────┐
│        MyWeatherController (REST API)        │
│        - Request routing & validation        │
└──────────────────────┬──────────────────────┘
                       │
┌──────────────────────▼──────────────────────┐
│        MyWeatherService (Business Logic)     │
│        - External API integration            │
│        - Data transformation                 │
└──────────────────────┬──────────────────────┘
                       │ HTTP (RestTemplate)
┌──────────────────────▼──────────────────────┐
│        OpenWeatherMap External API            │
└──────────────────────────────────────────────┘
```

**Key design decisions:**
- `RestTemplate` is configured as a Spring Bean for testability and reuse
- Controllers delegate all business logic to service classes
- Spring Boot Actuator is included for health checks and monitoring

---

## Project Structure

```
weather-service/
├── src/
│   ├── main/
│   │   ├── java/com/example/demo/
│   │   │   ├── MyWeatherApplication.java      # Spring Boot entry point + Bean config
│   │   │   ├── MyWeatherController.java       # REST controller (request handling)
│   │   │   └── MyWeatherService.java          # Business logic (API integration)
│   │   └── resources/
│   │       ├── application.properties         # Application configuration
│   │       └── static/
│   │           └── index.html                 # Static welcome page
│   └── test/
│       └── java/com/example/demo/
│           └── MyWeatherApplicationTests.java # Integration test class
├── pom.xml                                    # Maven build configuration
├── mvnw / mvnw.cmd                            # Maven wrapper scripts (Linux/Windows)
├── AI_GUIDELINES.md                           # This file
└── README.md                                  # Project README
```

---

## Build and Run Commands

| Action | Command |
|--------|---------|
| Build the project | `./mvnw clean package` |
| Run tests only | `./mvnw test` |
| Run the application | `./mvnw spring-boot:run` |
| Skip tests during build | `./mvnw clean package -DskipTests` |
| Clean build artifacts | `./mvnw clean` |
| Generate dependency tree | `./mvnw dependency:tree` |
| Check for dependency updates | `./mvnw versions:display-dependency-updates` |

> **Important**: Always use `./mvnw` (or `mvnw.cmd` on Windows) instead of a system-installed Maven to ensure consistent, reproducible builds.

---

## API Reference

### GET `/weather`

Fetches current weather data for a specified city.

**Parameters:**

| Name | Type | Required | Description |
|------|------|----------|-------------|
| `city` | String (query param) | Yes | City name (e.g., `London`, `New York`) |

**Example Request:**
```
GET /weather?city=London
```

**Example Response:**
```json
{
  "coord": {"lon": -0.13, "lat": 51.51},
  "weather": [{"id": 300, "main": "Drizzle", "description": "light intensity drizzle"}],
  "main": {"temp": 280.32, "pressure": 1012, "humidity": 81},
  "name": "London"
}
```

**Error Scenarios:**
- Missing `city` parameter → 400 Bad Request
- Invalid city name → 404/500 (propagated from upstream API)
- OpenWeatherMap API unavailable → 500 Internal Server Error

### Actuator Endpoints

Spring Boot Actuator is enabled, providing:

| Endpoint | Description |
|----------|-------------|
| `/actuator/health` | Application health status |
| `/actuator/info` | Application info |

---

## Coding Conventions

### General

- Follow standard Java naming conventions:
  - `camelCase` for methods and variables
  - `PascalCase` for classes and interfaces
  - `UPPER_SNAKE_CASE` for constants
- Use the `com.example.demo` package structure
- Keep controller, service, and application layers clearly separated
- Favor constructor injection over field injection (`@Autowired` on fields) for new code
- Use `final` for fields that should not be reassigned

### REST API Layer

- Controllers must be annotated with `@RestController`
- Use specific HTTP method annotations (`@GetMapping`, `@PostMapping`, `@PutMapping`, `@DeleteMapping`) over generic `@RequestMapping`
- Keep controller methods thin — delegate logic to service classes
- Use `@RequestParam` with explicit parameter names for clarity
- Return `ResponseEntity<T>` for fine-grained control over HTTP responses

### Service Layer

- Annotate service classes with `@Service`
- Services should encapsulate all business logic and external API calls
- Use constructor-based dependency injection for better testability
- Avoid direct HTTP calls in controllers — always route through services

### Bean Configuration

- Define reusable infrastructure beans (e.g., `RestTemplate`) in the main application class or dedicated `@Configuration` classes
- Use `RestTemplateBuilder` for creating `RestTemplate` beans (allows customization)

---

## Security Guidelines

### API Keys and Secrets

> ⚠️ **Critical**: Never hardcode API keys, passwords, or secrets in source code.

- Store secrets in `application.properties` or environment variables
- Use Spring's `@Value("${property.name}")` to inject configuration values
- Add sensitive property files to `.gitignore`
- For production, use environment variables or a secrets manager

**Current issue to address**: The OpenWeatherMap API key in `MyWeatherService.java` should be externalized to `application.properties`:

```java
// ✅ Preferred approach
@Value("${weather.api.key}")
private String apiKey;
```

```properties
# application.properties
weather.api.key=${WEATHER_API_KEY:your-default-dev-key}
```

### CORS Configuration

- Only enable `@CrossOrigin` when explicitly required
- Configure allowed origins specifically rather than using wildcards in production
- Prefer global CORS configuration in a `WebMvcConfigurer` for consistency

### Input Validation

- Validate all request parameters before processing
- Use `@Valid` and Bean Validation annotations where appropriate
- Never trust client input — sanitize before use in URLs or queries

---

## Error Handling

### Recommended Patterns

1. **Use `@ControllerAdvice`** for global exception handling:

```java
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<String> handleClientError(HttpClientErrorException ex) {
        return ResponseEntity.status(ex.getStatusCode()).body(ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericError(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body("An unexpected error occurred");
    }
}
```

2. **Wrap external API calls** in try-catch blocks to provide meaningful error responses
3. **Never expose internal stack traces** to API consumers
4. **Use appropriate HTTP status codes** (4xx for client errors, 5xx for server errors)

---

## Logging

### Conventions

- Use SLF4J (`org.slf4j.Logger`) — already included via Spring Boot
- Declare loggers as instance fields: `Logger logger = LoggerFactory.getLogger(this.getClass());`
- Use parameterized messages (avoid string concatenation):

```java
// ✅ Good
logger.info("Fetching weather for city: {}", city);

// ❌ Avoid
logger.info("Fetching weather for city: " + city);
```

### Log Levels

| Level | Use for |
|-------|---------|
| `ERROR` | Failures that need immediate attention |
| `WARN` | Unexpected conditions that are recoverable |
| `INFO` | Key business events (API calls, startup) |
| `DEBUG` | Detailed flow for troubleshooting |

### Sensitive Data

- **Never log API keys, passwords, or tokens**
- Avoid logging full request/response bodies that may contain PII
- Mask or truncate sensitive fields before logging

---

## Testing

### Test Organization

- Place test classes under `src/test/java` with the same package structure as main sources
- Name test classes with the `Tests` suffix (e.g., `MyWeatherServiceTests`)
- Use Spring Boot's test annotations:
  - `@SpringBootTest` for integration tests
  - `@RunWith(SpringRunner.class)` for JUnit 4 + Spring
  - `@WebMvcTest` for controller-only tests
  - `@MockBean` for mocking dependencies

### Test Patterns

```java
@RunWith(SpringRunner.class)
@SpringBootTest
public class MyWeatherServiceTests {

    @MockBean
    private RestTemplate restTemplate;

    @Autowired
    private MyWeatherService weatherService;

    @Test
    public void getWeather_validCity_returnsResponse() {
        // Given
        when(restTemplate.getForEntity(any(URI.class), eq(String.class)))
            .thenReturn(ResponseEntity.ok("{\"name\":\"London\"}"));

        // When
        ResponseEntity<String> response = weatherService.getWeather("London");

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }
}
```

### Test Guidelines

- Write tests for both happy paths and error scenarios
- Mock external API calls (`RestTemplate`) to avoid flaky tests
- Ensure all tests pass before submitting changes: `./mvnw test`
- Aim for meaningful test coverage of service-layer logic

---

## Configuration Management

### application.properties

All externalized configuration belongs in `src/main/resources/application.properties`:

```properties
# Server configuration
server.port=8080

# Weather API configuration
weather.api.base-url=http://api.openweathermap.org/data/2.5
weather.api.key=${WEATHER_API_KEY}
weather.api.units=metric

# Actuator configuration
management.endpoints.web.exposure.include=health,info

# Logging
logging.level.com.example.demo=INFO
```

### Profiles

- Use Spring profiles for environment-specific configuration (`application-dev.properties`, `application-prod.properties`)
- Activate with `--spring.profiles.active=dev`

---

## Dependency Management

### Current Dependencies

| Dependency | Purpose |
|------------|---------|
| `spring-boot-starter-web` | Web MVC, embedded Tomcat, REST support |
| `spring-boot-starter-actuator` | Health checks, metrics, monitoring |
| `spring-boot-starter-test` | Test framework (JUnit, Mockito, Spring Test) |

### Adding Dependencies

- Dependencies are managed through the Spring Boot parent BOM — omit version tags when possible
- Add new dependencies to the `<dependencies>` section in `pom.xml`
- Prefer Spring Boot starters over raw library dependencies
- Always specify `<scope>test</scope>` for test-only dependencies

---

## Performance Considerations

- **Connection pooling**: Consider configuring a connection pool for `RestTemplate` (e.g., Apache HttpClient) for production use
- **Caching**: Weather data is suitable for short-lived caching (`@Cacheable`) to reduce upstream API calls
- **Timeouts**: Configure `RestTemplate` connection and read timeouts to prevent thread exhaustion:

```java
@Bean
public RestTemplate restTemplate(RestTemplateBuilder builder) {
    return builder
        .setConnectTimeout(Duration.ofSeconds(5))
        .setReadTimeout(Duration.ofSeconds(10))
        .build();
}
```

- **Rate limiting**: Be mindful of OpenWeatherMap API rate limits on the free tier

---

## Contribution Workflow

1. **Branch**: Create a feature branch from `master` with a descriptive name:
   - `feature/add-forecast-endpoint`
   - `fix/handle-invalid-city`
   - `refactor/externalize-api-key`

2. **Develop**: Make changes following all conventions in this document

3. **Test**: Run the full test suite:
   ```bash
   ./mvnw clean test
   ```

4. **Commit**: Use conventional commit messages:
   - `feat: add 5-day forecast endpoint`
   - `fix: handle null city parameter gracefully`
   - `docs: update API reference in README`
   - `refactor: externalize API key to properties`

5. **Pull Request**: Submit a PR with:
   - Clear title following the commit convention
   - Description of what changed and why
   - Any testing notes or screenshots if applicable

---

## Common Pitfalls

| Pitfall | Guidance |
|---------|----------|
| Using Java 9+ features | This project targets **Java 8** — no `var`, no modules, no `List.of()` |
| Using Spring Boot 2.2+ APIs | Stick to **Spring Boot 2.1.x** compatible APIs |
| Hardcoding configuration | Always externalize to `application.properties` or env vars |
| Field injection in new code | Prefer constructor injection for testability |
| Ignoring error responses from external APIs | Always handle `RestClientException` and its subclasses |
| Committing to `master` directly | Always use feature branches and PRs |
| Using `@RequestMapping` without HTTP method | Use `@GetMapping`, `@PostMapping`, etc. for clarity |
| Logging sensitive data | Never log API keys, tokens, or PII |
