# AI Guidelines for Weather Service

This document provides comprehensive guidelines for AI assistants working with this codebase. All guidance is specific to this project's actual stack: Java 8, Spring Boot 2.1.2.RELEASE, and Maven.

---

## Table of Contents

1. [Project Overview](#project-overview)
2. [Environment Setup](#environment-setup)
3. [Project Structure](#project-structure)
4. [Architecture Decisions](#architecture-decisions)
5. [Build and Run Commands](#build-and-run-commands)
6. [Configuration Management](#configuration-management)
7. [Coding Conventions](#coding-conventions)
8. [Error Handling Patterns](#error-handling-patterns)
9. [Logging Conventions](#logging-conventions)
10. [API Response Format](#api-response-format)
11. [Security Considerations](#security-considerations)
12. [Performance Guidelines](#performance-guidelines)
13. [Testing Strategy](#testing-strategy)
14. [Code Quality](#code-quality)
15. [Dependency Management](#dependency-management)
16. [Monitoring and Health Checks](#monitoring-and-health-checks)
17. [Git Conventions](#git-conventions)
18. [Troubleshooting](#troubleshooting)

---

## Project Overview

- **Type**: Java/Spring Boot REST API application
- **Build Tool**: Maven (with Maven Wrapper - `mvnw`)
- **Java Version**: 1.8 (do NOT use Java 9+ features)
- **Spring Boot Version**: 2.1.2.RELEASE
- **Purpose**: A weather service that proxies requests to the OpenWeatherMap API
- **Group ID**: `com.harish`
- **Artifact ID**: `myWeather`
- **Base Package**: `com.example.demo`

### What This Application Does

The service exposes a REST endpoint that accepts a city name and returns weather data by calling the OpenWeatherMap external API (`api.openweathermap.org`). It uses Spring's `RestTemplate` for outbound HTTP calls and returns the raw JSON response from OpenWeatherMap to the client.

---

## Environment Setup

### Prerequisites

- **JDK 8** (1.8.x) - not JDK 9 or higher
- **No Maven installation required** - the Maven Wrapper (`./mvnw`) handles this
- **Network access** to `api.openweathermap.org` for runtime (not required for build/test)

### IDE Setup

- Import as a Maven project
- Set project SDK to Java 8
- Source encoding: UTF-8
- Enable annotation processing for Spring annotations
- Set the main class to `com.example.demo.MyWeatherApplication`

### First-Time Setup

```bash
# Clone the repository
git clone <repo-url>
cd weather-service

# Verify build (no network needed for build)
./mvnw clean package

# Run the application (needs network for OpenWeatherMap API calls)
./mvnw spring-boot:run
```

The application starts on the default port `8080` (configurable via `application.properties`).

---

## Project Structure

```
weather-service/
├── src/
│   ├── main/
│   │   ├── java/com/example/demo/
│   │   │   ├── MyWeatherApplication.java    # Spring Boot entry point + bean config
│   │   │   ├── MyWeatherController.java     # REST controller (HTTP layer)
│   │   │   └── MyWeatherService.java        # Business logic + external API calls
│   │   └── resources/
│   │       ├── application.properties       # Application configuration (currently empty)
│   │       └── static/
│   │           └── index.html               # Static welcome page
│   └── test/
│       └── java/com/example/demo/
│           └── MyWeatherApplicationTests.java  # Spring Boot context load test
├── pom.xml                                  # Maven build configuration
├── mvnw / mvnw.cmd                          # Maven wrapper scripts (Linux/Windows)
├── .mvn/                                    # Maven wrapper JAR and properties
├── AI_GUIDELINES.md                         # This file
└── README.md
```

---

## Architecture Decisions

### Layered Architecture

The project follows a simple two-layer architecture:

1. **Controller Layer** (`MyWeatherController`) - Handles HTTP request/response mapping
2. **Service Layer** (`MyWeatherService`) - Contains business logic and external API integration

There is no repository/persistence layer since this service acts as a proxy.

### Key Design Choices

| Decision | Rationale |
|----------|-----------|
| `RestTemplate` over WebClient | Spring Boot 2.1.x era; WebClient requires WebFlux dependency. RestTemplate is the standard synchronous HTTP client for this version. |
| Bean-configured RestTemplate | Defined as a `@Bean` in the application class using `RestTemplateBuilder`, allowing Spring to manage its lifecycle and enabling easy mocking in tests. |
| Direct string response | The service returns `ResponseEntity<String>` raw JSON from OpenWeatherMap rather than deserializing into a domain model. This keeps the proxy thin. |
| No persistence layer | The application is a stateless proxy; no database or caching is currently implemented. |

### When Adding New Features

- New REST endpoints: create a new `@RestController` class
- New external service integrations: create a new `@Service` class
- Shared configuration beans: add to `MyWeatherApplication.java` or create a `@Configuration` class
- Domain models: create in a `model` sub-package (e.g., `com.example.demo.model`)

---

## Build and Run Commands

| Action | Command |
|--------|---------|
| Build the project | `./mvnw clean package` |
| Run tests only | `./mvnw test` |
| Run the application | `./mvnw spring-boot:run` |
| Skip tests during build | `./mvnw clean package -DskipTests` |
| Run a single test class | `./mvnw test -Dtest=MyWeatherApplicationTests` |
| Generate dependency tree | `./mvnw dependency:tree` |
| Clean build artifacts | `./mvnw clean` |
| Package without running | `./mvnw package -DskipTests` |
| Run the JAR directly | `java -jar target/myWeather-0.0.1-SNAPSHOT.jar` |

### Build Output

- Compiled classes: `target/classes/`
- Test classes: `target/test-classes/`
- Packaged JAR: `target/myWeather-0.0.1-SNAPSHOT.jar`

---

## Configuration Management

### Current State

The `application.properties` file is currently empty. All configuration uses Spring Boot defaults:
- Server port: `8080`
- Context path: `/`
- Actuator endpoints: default exposure

### Configuration Best Practices for This Project

```properties
# Server configuration
server.port=8080

# External API configuration (should be externalized)
weather.api.base-url=http://api.openweathermap.org/data/2.5/weather
weather.api.key=${WEATHER_API_KEY:d5b5488bbfe859639c0b208f29538344}
weather.api.units=metric

# Actuator configuration
management.endpoints.web.exposure.include=health,info,metrics

# Logging
logging.level.com.example.demo=INFO
logging.level.org.springframework.web.client.RestTemplate=DEBUG
```

### Profile-Based Configuration

Spring Boot 2.1.x supports profiles via:
- `application-{profile}.properties` files
- Activated with `--spring.profiles.active=dev`
- Example profiles: `dev`, `test`, `prod`

```
src/main/resources/
├── application.properties          # Shared/default config
├── application-dev.properties      # Development overrides
├── application-test.properties     # Test overrides
└── application-prod.properties     # Production overrides
```

### Sensitive Configuration

The OpenWeatherMap API key is currently hardcoded in `MyWeatherService.java`. When refactoring:
- Move to `application.properties` with `@Value("${weather.api.key}")` injection
- Use environment variables for production: `${WEATHER_API_KEY}`
- Never commit real API keys to the repository

---

## Coding Conventions

### General Java Style

- Follow standard Java naming conventions:
  - `camelCase` for methods and variables
  - `PascalCase` for classes and interfaces
  - `UPPER_SNAKE_CASE` for constants
- Use the `com.example.demo` base package
- Keep controller, service, and application layers separated
- Use Spring annotations (`@RestController`, `@Service`, `@SpringBootApplication`)
- Prefer constructor injection over field injection with `@Autowired` (current code uses field injection; new code should prefer constructor injection)

### Java 8 Compatibility Rules

This project targets Java 8. The following are NOT available:

| Not Available (Java 9+) | Use Instead |
|--------------------------|-------------|
| `var` keyword | Explicit type declarations |
| `List.of()`, `Map.of()` | `Arrays.asList()`, `Collections.unmodifiableList()` |
| `String.isBlank()` | `string.trim().isEmpty()` |
| `Optional.ifPresentOrElse()` | `if (opt.isPresent()) ... else ...` |
| Text blocks (`"""`) | Standard string concatenation |
| Records | Regular classes with getters/setters |
| Sealed classes | Standard class hierarchies |
| `HttpClient` (java.net.http) | Spring's `RestTemplate` |
| Modules (module-info.java) | Classpath-based dependencies |

What IS available in Java 8:
- Lambda expressions and method references
- Streams API (`java.util.stream`)
- `Optional<T>`
- `CompletableFuture`
- Default methods in interfaces
- `java.time` (LocalDate, LocalDateTime, etc.)

### Spring Boot 2.1.x Specifics

- Use `@RunWith(SpringRunner.class)` for tests (JUnit 4 style, not JUnit 5)
- `RestTemplate` is the primary HTTP client (not `WebClient`)
- `@RequestMapping` is used (though `@GetMapping`/`@PostMapping` are preferred for clarity)
- Test with `@SpringBootTest` and `@MockBean`
- Bean validation uses `javax.validation` (not `jakarta.validation`)

### REST API Design

- Controllers annotated with `@RestController`
- Use specific HTTP method annotations (`@GetMapping`, `@PostMapping`, etc.) over generic `@RequestMapping` where possible
- Keep controller methods focused; delegate business logic to service classes
- Return `ResponseEntity<T>` for explicit control over HTTP status codes and headers
- Use `@RequestParam` for query parameters, `@PathVariable` for path segments

### Dependency Injection

Current pattern (field injection):
```java
@Autowired
private MyWeatherService myWeatherService;
```

Preferred pattern for new code (constructor injection):
```java
private final MyWeatherService myWeatherService;

public MyWeatherController(MyWeatherService myWeatherService) {
    this.myWeatherService = myWeatherService;
}
```

Constructor injection is preferred because:
- Makes dependencies explicit
- Enables immutability (`final` fields)
- Simplifies unit testing without Spring context

---

## Error Handling Patterns

### Current State

The application currently has no explicit error handling. If the external API call fails, Spring's default error handling returns a generic 500 error.

### Recommended Pattern: `@ControllerAdvice`

Create a global exception handler:

```java
package com.example.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<String> handleHttpClientError(HttpClientErrorException ex) {
        logger.error("External API error: {} - {}", ex.getStatusCode(), ex.getMessage());
        return ResponseEntity.status(ex.getStatusCode()).body(ex.getResponseBodyAsString());
    }

    @ExceptionHandler(ResourceAccessException.class)
    public ResponseEntity<String> handleConnectionError(ResourceAccessException ex) {
        logger.error("Connection error to external service: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
            .body("{\"error\": \"Weather service temporarily unavailable\"}");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericError(Exception ex) {
        logger.error("Unexpected error: ", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body("{\"error\": \"Internal server error\"}");
    }
}
```

### Exception Hierarchy for This Project

| Exception Type | HTTP Status | When |
|---------------|-------------|------|
| `HttpClientErrorException` (4xx from OpenWeatherMap) | Pass through (e.g., 404 for unknown city) | City not found, invalid API key |
| `HttpServerErrorException` (5xx from OpenWeatherMap) | 502 Bad Gateway | Upstream service failure |
| `ResourceAccessException` | 503 Service Unavailable | Network timeout, connection refused |
| `IllegalArgumentException` | 400 Bad Request | Missing or invalid city parameter |
| `Exception` (catch-all) | 500 Internal Server Error | Unexpected failures |

### Input Validation

Validate the `city` parameter before calling the external API:

```java
@RequestMapping(value = "/weather")
public ResponseEntity<String> getWeather(@RequestParam("city") String city) {
    if (city == null || city.trim().isEmpty()) {
        return ResponseEntity.badRequest().body("{\"error\": \"City parameter is required\"}");
    }
    if (city.length() > 100) {
        return ResponseEntity.badRequest().body("{\"error\": \"City name too long\"}");
    }
    return myWeatherService.getWeather(city.trim());
}
```

---

## Logging Conventions

### Framework

This project uses **SLF4J** with **Logback** (included via `spring-boot-starter-web`).

### Logger Declaration Pattern

```java
// Current pattern in the codebase (instance-level logger)
Logger logger = LoggerFactory.getLogger(this.getClass());

// Preferred pattern for new code (static final)
private static final Logger logger = LoggerFactory.getLogger(MyWeatherService.class);
```

Use `private static final` for consistency and minor performance benefit.

### Log Level Guidelines

| Level | Use For | Example |
|-------|---------|---------|
| `ERROR` | Failures requiring attention; caught exceptions that indicate a problem | `logger.error("Failed to fetch weather for city: {}", city, ex)` |
| `WARN` | Recoverable issues; degraded functionality | `logger.warn("Retrying weather API call, attempt {}", retryCount)` |
| `INFO` | Significant business events; request flow | `logger.info("Weather request for city: {}", city)` |
| `DEBUG` | Detailed diagnostic info; full URLs, payloads | `logger.debug("Calling URL: {}", builder.toUriString())` |
| `TRACE` | Very fine-grained; method entry/exit | `logger.trace("Entering getWeather()")` |

### Logging Best Practices

1. **Use parameterized messages** (SLF4J placeholder syntax):
   ```java
   // CORRECT - lazy evaluation, no string concatenation cost if level is disabled
   logger.info("Weather request for city: {}", city);

   // INCORRECT - string concatenation happens regardless of log level
   logger.info("Weather request for city: " + city);
   ```

2. **Include exception as last argument** for stack traces:
   ```java
   logger.error("Failed to call weather API: {}", ex.getMessage(), ex);
   ```

3. **Never log sensitive data**: API keys, tokens, passwords, or personal information.

4. **Log at method boundaries** for external calls:
   ```java
   logger.info("Calling OpenWeatherMap API for city: {}", city);
   // ... API call ...
   logger.info("Received response with status: {}", response.getStatusCode());
   ```

### Configuration

In `application.properties`:
```properties
# Set root level
logging.level.root=WARN

# Application-level logging
logging.level.com.example.demo=INFO

# Show RestTemplate request/response for debugging
logging.level.org.springframework.web.client.RestTemplate=DEBUG

# Log file output (optional)
logging.file=logs/weather-service.log
logging.file.max-size=10MB
logging.file.max-history=7
```

---

## API Response Format

### Current Behavior

The API currently returns the raw JSON string from OpenWeatherMap without any wrapping. The response content type is determined by Spring's default content negotiation.

### Endpoints

| Method | Path | Parameters | Response |
|--------|------|------------|----------|
| GET | `/` (mapped via `@RequestMapping(name="/weather")`) | `city` (required query param) | OpenWeatherMap JSON response |
| GET | `/` | Static content | `index.html` welcome page |

**Note**: There is a bug in the current controller - `@RequestMapping(name="/weather")` sets the mapping *name* (for URL generation), not the path. It should be `@RequestMapping(value="/weather")` or `@GetMapping("/weather")` to properly map the path.

### HTTP Status Code Usage

| Status | Meaning | When to Use |
|--------|---------|-------------|
| 200 | Success | Weather data retrieved successfully |
| 400 | Bad Request | Missing or invalid `city` parameter |
| 404 | Not Found | City not found in OpenWeatherMap |
| 429 | Too Many Requests | API rate limit exceeded |
| 500 | Internal Server Error | Unexpected application error |
| 502 | Bad Gateway | OpenWeatherMap returned a 5xx error |
| 503 | Service Unavailable | Cannot reach OpenWeatherMap (timeout/connection error) |

### Recommended Standardized Error Response Format

```json
{
  "error": {
    "code": "CITY_NOT_FOUND",
    "message": "No weather data found for city: XYZ",
    "timestamp": "2024-01-15T10:30:00Z",
    "path": "/weather"
  }
}
```

---

## Security Considerations

### Current Vulnerabilities

1. **Hardcoded API key** in `MyWeatherService.java` - should be externalized to environment variables
2. **No input sanitization** on the `city` parameter - could be used for SSRF if URL construction is not careful
3. **CORS is commented out** (`@CrossOrigin` annotation is commented) - decide intentionally whether to enable
4. **No rate limiting** - the service could be used to exhaust the OpenWeatherMap API quota

### Security Best Practices for This Project

1. **Externalize secrets**:
   ```properties
   # application.properties
   weather.api.key=${WEATHER_API_KEY}
   ```

2. **Validate and sanitize input**:
   ```java
   // Reject suspicious input
   if (city.contains("http") || city.contains("://") || city.matches(".*[<>\"'&].*")) {
       return ResponseEntity.badRequest().body("{\"error\": \"Invalid city name\"}");
   }
   ```

3. **Add request timeouts** to RestTemplate:
   ```java
   @Bean
   public RestTemplate restTemplate(RestTemplateBuilder builder) {
       return builder
           .setConnectTimeout(Duration.ofSeconds(5))
           .setReadTimeout(Duration.ofSeconds(10))
           .build();
   }
   ```
   Note: In Spring Boot 2.1.x, use `Duration` with `setConnectTimeout`/`setReadTimeout`.

4. **CORS configuration** - if enabling, be explicit about allowed origins:
   ```java
   @CrossOrigin(origins = "http://localhost:3000")
   ```

5. **Add Spring Security** (if needed) via `spring-boot-starter-security` dependency.

### Headers to Consider

```java
// Add security headers via a filter or interceptor
response.setHeader("X-Content-Type-Options", "nosniff");
response.setHeader("X-Frame-Options", "DENY");
response.setHeader("Cache-Control", "no-store");
```

---

## Performance Guidelines

### RestTemplate Configuration

The current `RestTemplate` bean uses default settings. For production:

```java
@Bean
public RestTemplate restTemplate(RestTemplateBuilder builder) {
    return builder
        .setConnectTimeout(Duration.ofSeconds(5))   // Fail fast on connection issues
        .setReadTimeout(Duration.ofSeconds(10))     // Don't hang on slow responses
        .build();
}
```

### Caching (Optional Enhancement)

Since weather data does not change every second, consider caching:

```java
// Add to pom.xml: spring-boot-starter-cache
// Enable in application class: @EnableCaching

@Cacheable(value = "weather", key = "#city")
public ResponseEntity<String> getWeather(String city) {
    // ... existing implementation
}
```

Configure cache TTL in `application.properties`:
```properties
spring.cache.caffeine.spec=maximumSize=100,expireAfterWrite=300s
```

### Connection Pooling

The default `RestTemplate` creates a new connection per request. For high-throughput:

```java
@Bean
public RestTemplate restTemplate() {
    HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
    factory.setConnectTimeout(5000);
    factory.setReadTimeout(10000);
    // Requires org.apache.httpcomponents:httpclient dependency
    return new RestTemplate(factory);
}
```

### Async Patterns (If Needed)

For non-blocking calls in Spring Boot 2.1.x without WebFlux:

```java
@Async
public CompletableFuture<ResponseEntity<String>> getWeatherAsync(String city) {
    ResponseEntity<String> result = getWeather(city);
    return CompletableFuture.completedFuture(result);
}
```

Requires `@EnableAsync` on the application class.

---

## Testing Strategy

### Current Test Coverage

The project currently has a single test:
- `MyWeatherApplicationTests.contextLoads()` - verifies the Spring context loads successfully

### Test Types to Implement

#### 1. Unit Tests (Service Layer)

Test `MyWeatherService` by mocking the `RestTemplate`:

```java
@RunWith(MockitoJUnitRunner.class)
public class MyWeatherServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private MyWeatherService weatherService;

    @Test
    public void getWeather_validCity_returnsResponse() {
        ResponseEntity<String> mockResponse = ResponseEntity.ok("{\"temp\": 20}");
        when(restTemplate.getForEntity(any(URI.class), eq(String.class)))
            .thenReturn(mockResponse);

        ResponseEntity<String> result = weatherService.getWeather("London");

        assertEquals(HttpStatus.OK, result.getStatusCode());
        verify(restTemplate).getForEntity(any(URI.class), eq(String.class));
    }

    @Test(expected = ResourceAccessException.class)
    public void getWeather_connectionTimeout_throwsException() {
        when(restTemplate.getForEntity(any(URI.class), eq(String.class)))
            .thenThrow(new ResourceAccessException("Connection timeout"));

        weatherService.getWeather("London");
    }
}
```

#### 2. Controller Tests (MockMvc)

Test `MyWeatherController` with Spring MockMvc:

```java
@RunWith(SpringRunner.class)
@WebMvcTest(MyWeatherController.class)
public class MyWeatherControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MyWeatherService weatherService;

    @Test
    public void getWeather_withCity_returnsOk() throws Exception {
        when(weatherService.getWeather("London"))
            .thenReturn(ResponseEntity.ok("{\"temp\": 20}"));

        mockMvc.perform(get("/weather").param("city", "London"))
            .andExpect(status().isOk())
            .andExpect(content().json("{\"temp\": 20}"));
    }

    @Test
    public void getWeather_missingCity_returnsBadRequest() throws Exception {
        mockMvc.perform(get("/weather"))
            .andExpect(status().isBadRequest());
    }
}
```

#### 3. Integration Tests

Test the full Spring context with mocked external dependencies:

```java
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WeatherIntegrationTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @MockBean
    private RestTemplate restTemplate; // Mock the external API call

    @Test
    public void fullFlow_validCity_returnsWeather() {
        when(restTemplate.getForEntity(any(URI.class), eq(String.class)))
            .thenReturn(ResponseEntity.ok("{\"main\":{\"temp\":15}}"));

        ResponseEntity<String> response = testRestTemplate
            .getForEntity("/weather?city=London", String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
```

### Test Naming Convention

Use descriptive names following the pattern: `methodName_scenario_expectedBehavior`

Examples:
- `getWeather_validCity_returnsOk`
- `getWeather_emptyCity_returnsBadRequest`
- `getWeather_serviceUnavailable_returns503`

### Test File Locations

```
src/test/java/com/example/demo/
├── MyWeatherApplicationTests.java      # Context load test (existing)
├── MyWeatherControllerTest.java        # Controller unit tests
├── MyWeatherServiceTest.java           # Service unit tests
└── WeatherIntegrationTest.java         # Full integration tests
```

### Running Tests

```bash
# Run all tests
./mvnw test

# Run a specific test class
./mvnw test -Dtest=MyWeatherServiceTest

# Run a specific test method
./mvnw test -Dtest=MyWeatherServiceTest#getWeather_validCity_returnsResponse

# Run with debug output
./mvnw test -X
```

---

## Code Quality

### Anti-Patterns to Avoid

| Anti-Pattern | Found In Codebase | Correction |
|--------------|-------------------|------------|
| Hardcoded secrets | `MyWeatherService` (API key in source) | Use `@Value` with env variable fallback |
| Field injection | `MyWeatherController`, `MyWeatherService` | Use constructor injection |
| `@RequestMapping(name=...)` for path | `MyWeatherController` | Use `@GetMapping("/weather")` or `@RequestMapping(value="/weather")` |
| No error handling | Throughout | Add `@ControllerAdvice` |
| No input validation | `MyWeatherController` | Validate parameters before processing |
| Instance logger | `MyWeatherService` | Use `private static final Logger` |
| String concatenation in log | `MyWeatherService` (`"URL "+ builder...`) | Use SLF4J placeholders: `logger.info("URL {}", ...)` |

### Static Analysis Tools (Optional)

For enhanced code quality, consider adding to `pom.xml`:

```xml
<!-- SpotBugs (successor to FindBugs, Java 8 compatible) -->
<plugin>
    <groupId>com.github.spotbugs</groupId>
    <artifactId>spotbugs-maven-plugin</artifactId>
    <version>3.1.12</version>
</plugin>

<!-- Checkstyle -->
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-checkstyle-plugin</artifactId>
    <version>3.1.0</version>
</plugin>
```

Run with:
```bash
./mvnw spotbugs:check
./mvnw checkstyle:check
```

### Code Coverage

Add JaCoCo for coverage reporting:

```xml
<plugin>
    <groupId>org.jacoco</groupId>
    <artifactId>jacoco-maven-plugin</artifactId>
    <version>0.8.5</version>
    <executions>
        <execution>
            <goals><goal>prepare-agent</goal></goals>
        </execution>
        <execution>
            <id>report</id>
            <phase>test</phase>
            <goals><goal>report</goal></goals>
        </execution>
    </executions>
</plugin>
```

Coverage report generated at: `target/site/jacoco/index.html`

---

## Dependency Management

### Current Dependencies

| Dependency | Purpose |
|------------|---------|
| `spring-boot-starter-web` | REST API, embedded Tomcat, Jackson JSON |
| `spring-boot-starter-actuator` | Health checks, metrics, info endpoints |
| `spring-boot-starter-test` | JUnit 4, Mockito, Spring Test, MockMvc |

### Adding Dependencies

1. Check if a Spring Boot starter exists first (e.g., `spring-boot-starter-cache` over raw Caffeine)
2. Spring Boot BOM manages versions - omit `<version>` for managed dependencies
3. Test dependencies use `<scope>test</scope>`
4. Verify Java 8 compatibility before adding any library

### Common Additions for This Project

```xml
<!-- If adding caching -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-cache</artifactId>
</dependency>

<!-- If adding validation -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-validation</artifactId>
</dependency>

<!-- If adding security -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>

<!-- For better RestTemplate with connection pooling -->
<dependency>
    <groupId>org.apache.httpcomponents</groupId>
    <artifactId>httpclient</artifactId>
</dependency>
```

### Version Compatibility Check

Before adding any dependency, verify:
1. Compatible with Java 8
2. Compatible with Spring Boot 2.1.x
3. Does not conflict with existing transitive dependencies (`./mvnw dependency:tree`)

---

## Monitoring and Health Checks

### Spring Boot Actuator (Already Included)

The project includes `spring-boot-starter-actuator`. Default endpoints available at `/actuator`:

| Endpoint | Purpose |
|----------|---------|
| `/actuator/health` | Application health status |
| `/actuator/info` | Application metadata |
| `/actuator/metrics` | Runtime metrics |
| `/actuator/env` | Environment properties (restricted) |
| `/actuator/beans` | All Spring beans |

### Configuration

```properties
# Expose specific endpoints (security best practice)
management.endpoints.web.exposure.include=health,info,metrics

# Custom health indicator details
management.endpoint.health.show-details=when-authorized

# Custom application info
info.app.name=Weather Service
info.app.version=0.0.1-SNAPSHOT
info.app.description=OpenWeatherMap proxy service
```

### Custom Health Indicator

Check connectivity to OpenWeatherMap:

```java
@Component
public class WeatherApiHealthIndicator implements HealthIndicator {

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public Health health() {
        try {
            restTemplate.getForEntity(
                "http://api.openweathermap.org/data/2.5/weather?q=London&APPID=<key>&units=metric",
                String.class);
            return Health.up().withDetail("openweathermap", "reachable").build();
        } catch (Exception e) {
            return Health.down().withDetail("openweathermap", e.getMessage()).build();
        }
    }
}
```

---

## Git Conventions

### Branch Naming

| Type | Format | Example |
|------|--------|---------|
| Feature | `feature/<short-description>` | `feature/add-caching` |
| Bug fix | `fix/<issue-description>` | `fix/timeout-handling` |
| Documentation | `docs/<what>` | `docs/update-api-docs` |
| Refactor | `refactor/<what>` | `refactor/extract-config` |

### Commit Message Format

Follow [Conventional Commits](https://www.conventionalcommits.org/):

```
<type>(<scope>): <short description>

<optional body with details>

<optional footer>
```

Types: `feat`, `fix`, `docs`, `refactor`, `test`, `chore`, `style`, `perf`

Examples:
```
feat(api): add city validation to weather endpoint

fix(service): handle connection timeout to OpenWeatherMap

docs: update AI guidelines with error handling patterns

test(controller): add MockMvc tests for weather endpoint

refactor(service): extract API configuration to properties
```

### Pull Request Guidelines

- Title follows the same commit message format
- Description should include:
  - What was changed and why
  - How to test the changes
  - Any breaking changes
  - Related issues (if applicable)
- Ensure all tests pass before opening a PR
- Keep PRs focused on a single concern

---

## Troubleshooting

### Common Issues

#### 1. `./mvnw: Permission denied`
```bash
chmod +x mvnw
```

#### 2. Build fails with "invalid source release: 1.8"
Your system Java version is too old. Ensure JDK 8 is installed and `JAVA_HOME` points to it:
```bash
export JAVA_HOME=/path/to/jdk1.8
```

#### 3. Application starts but `/weather` returns 404
The current controller uses `@RequestMapping(name="/weather")` which sets the mapping name, not the path. It should be `@RequestMapping(value="/weather")` or `@GetMapping("/weather")`.

#### 4. `org.springframework.web.client.ResourceAccessException`
The application cannot reach `api.openweathermap.org`. Check:
- Network connectivity
- Proxy settings (if behind a corporate firewall)
- DNS resolution

#### 5. Tests pass but application fails at runtime
The only existing test (`contextLoads`) verifies Spring context loads but does NOT test actual API calls. External service issues will only surface at runtime.

#### 6. `java.lang.UnsupportedClassVersionError`
A dependency or plugin was compiled for a newer Java version. Check `./mvnw dependency:tree` and ensure all dependencies support Java 8.

#### 7. Port 8080 already in use
```bash
# Find the process
lsof -i :8080
# Or change the port
./mvnw spring-boot:run -Dserver.port=8081
```

### Debugging Tips

1. **Enable debug logging**:
   ```bash
   ./mvnw spring-boot:run -Ddebug
   ```

2. **See auto-configuration report**:
   ```bash
   ./mvnw spring-boot:run --debug
   ```

3. **Test external API manually**:
   ```bash
   curl "http://api.openweathermap.org/data/2.5/weather?q=London&APPID=<key>&units=metric"
   ```

4. **Check actuator health**:
   ```bash
   curl http://localhost:8080/actuator/health
   ```

---

## Quick Reference

### Key Files to Modify

| When You Need To... | Modify |
|---------------------|--------|
| Add a new endpoint | Create new `@RestController` in `src/main/java/com/example/demo/` |
| Add business logic | Create new `@Service` class |
| Add a bean/config | `MyWeatherApplication.java` or new `@Configuration` class |
| Change server settings | `src/main/resources/application.properties` |
| Add a static page | `src/main/resources/static/` |
| Add a dependency | `pom.xml` under `<dependencies>` |
| Add a test | `src/test/java/com/example/demo/` |

### Pre-Commit Checklist

- [ ] Code compiles: `./mvnw compile`
- [ ] Tests pass: `./mvnw test`
- [ ] No hardcoded secrets
- [ ] Logger uses SLF4J placeholders (not string concatenation)
- [ ] New public methods have corresponding test coverage
- [ ] Java 8 compatible (no Java 9+ features)
- [ ] Input parameters validated
- [ ] Appropriate error handling in place
