# Style Guide

This document provides detailed coding style guidance for the Weather Service project. All contributors should follow these conventions to maintain a consistent, readable, and maintainable codebase.

## Project Structure

```
weather-service/
├── src/
│   ├── main/
│   │   ├── java/com/example/demo/
│   │   │   ├── controller/       # REST controllers
│   │   │   ├── service/          # Business logic
│   │   │   ├── model/            # Data models / DTOs
│   │   │   ├── config/           # Configuration classes
│   │   │   └── exception/        # Custom exceptions & handlers
│   │   └── resources/
│   │       ├── static/           # Static web content
│   │       └── application.properties
│   └── test/
│       └── java/com/example/demo/
├── pom.xml
├── .editorconfig
├── CONTRIBUTING.md
└── STYLE_GUIDE.md
```

## Java Style

### File Organization

Each Java source file should be organized in the following order:

1. Package statement
2. Import statements (no wildcard imports)
3. Class/interface declaration

### Import Ordering

Imports should be grouped and ordered as follows (with a blank line between groups):

1. `java.*`
2. `javax.*`
3. Third-party libraries (e.g., `org.springframework.*`, `org.slf4j.*`)
4. Project imports (`com.example.*`)

```java
package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.example.demo.model.WeatherResponse;
```

### Class Structure

Within a class, members should be ordered as:

1. Static constants
2. Instance fields
3. Constructors
4. Public methods
5. Package-private methods
6. Private methods

### Dependency Injection

Prefer **constructor injection** for required dependencies:

```java
// Preferred
@RestController
public class WeatherController {

    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }
}
```

```java
// Avoid
@RestController
public class WeatherController {

    @Autowired
    private WeatherService weatherService;
}
```

### REST Controller Style

- Use specific HTTP method annotations (`@GetMapping`, `@PostMapping`, etc.).
- Use meaningful path variables and request parameters.
- Return `ResponseEntity<T>` with appropriate status codes.
- Document endpoints with Javadoc.

```java
@RestController
@RequestMapping("/api/v1/weather")
public class WeatherController {

    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    /**
     * Retrieves current weather data for the specified city.
     *
     * @param city the city name to look up
     * @return weather data wrapped in a ResponseEntity
     */
    @GetMapping
    public ResponseEntity<WeatherResponse> getWeather(@RequestParam("city") String city) {
        WeatherResponse response = weatherService.getWeather(city);
        return ResponseEntity.ok(response);
    }
}
```

### Service Layer Style

- Keep services focused on a single responsibility.
- Use interfaces for services when multiple implementations are possible.
- Mark the logger as `private static final`:

```java
@Service
public class WeatherService {

    private static final Logger logger = LoggerFactory.getLogger(WeatherService.class);

    // ...
}
```

### Configuration Management

- **Never** hardcode API keys, passwords, or secrets in source code.
- Use `application.properties` or `application.yml` with environment variable placeholders:

```properties
weather.api.key=${WEATHER_API_KEY}
weather.api.base-url=https://api.openweathermap.org/data/2.5
```

- Create `@ConfigurationProperties` classes for grouped configuration:

```java
@ConfigurationProperties(prefix = "weather.api")
public class WeatherApiProperties {

    private String key;
    private String baseUrl;

    // getters and setters
}
```

## Testing Style

### Test Class Naming

- Unit tests: `<ClassName>Test.java`
- Integration tests: `<ClassName>IntegrationTest.java`

### Test Method Naming

Use descriptive method names that explain the scenario and expected outcome:

```java
@Test
void getWeather_withValidCity_returnsWeatherData() { }

@Test
void getWeather_withEmptyCity_throwsIllegalArgumentException() { }
```

### Test Structure

Follow the **Arrange-Act-Assert** (AAA) pattern:

```java
@Test
void getWeather_withValidCity_returnsWeatherData() {
    // Arrange
    String city = "London";
    WeatherResponse expected = new WeatherResponse(/* ... */);
    when(restTemplate.getForEntity(any(), eq(String.class))).thenReturn(/* ... */);

    // Act
    WeatherResponse actual = weatherService.getWeather(city);

    // Assert
    assertThat(actual).isEqualTo(expected);
}
```

### Test Coverage

- Aim for at least 80% code coverage on service and controller layers.
- Always test edge cases: null inputs, empty strings, API failures.
- Use `@MockBean` or Mockito for external service dependencies.

## Documentation Style

### Javadoc

- All public classes and methods should have Javadoc.
- Use `@param`, `@return`, and `@throws` tags.
- Keep descriptions concise but informative.

```java
/**
 * Retrieves current weather data from the OpenWeatherMap API.
 *
 * @param city the name of the city to query
 * @return a ResponseEntity containing the weather data as JSON
 * @throws WeatherServiceException if the external API call fails
 */
public ResponseEntity<String> getWeather(String city) { }
```

### Inline Comments

- Use sparingly; prefer self-documenting code.
- Explain **why**, not **what** (the code already shows what).

```java
// Retry with exponential backoff to handle transient API failures
for (int attempt = 0; attempt < MAX_RETRIES; attempt++) { }
```

## Git & Version Control

### Branch Naming

```
feature/<short-description>    # New features
fix/<short-description>        # Bug fixes
docs/<short-description>       # Documentation updates
refactor/<short-description>   # Code refactoring
```

### Commit Hygiene

- Make small, focused commits.
- Each commit should compile and pass tests.
- Squash WIP commits before merging.

## IDE Setup

### IntelliJ IDEA

1. Import the `.editorconfig` (auto-detected).
2. Set code style to Google Java Style (Settings > Editor > Code Style > Java > Import Scheme).
3. Enable "Optimize imports on the fly" and "Add unambiguous imports on the fly".

### VS Code

1. Install the "EditorConfig for VS Code" extension.
2. Install "Language Support for Java" extension pack.
3. The `.editorconfig` file will be applied automatically.
