# weather-service

## Styling Guidelines

This section documents the coding style conventions and patterns used throughout the weather-service codebase.

### 1. Naming Conventions

- **Classes**: PascalCase with a descriptive `My` prefix (e.g., `MyWeatherApplication`, `MyWeatherController`, `MyWeatherService`).
- **Methods**: camelCase (e.g., `getWeather`, `contextLoads`, `restTemplate`).
- **Variables**: camelCase (e.g., `restTemplate`, `myWeatherService`, `builder`).
- **Packages**: All lowercase, dot-separated (e.g., `com.example.demo`).

### 2. File Organization

- **Directory layout**: Standard Maven structure:
  - `src/main/java` - Application source code
  - `src/main/resources` - Configuration files (e.g., `application.properties`)
  - `src/test/java` - Test source code
- **Package structure**: All classes reside in a single flat package (`com.example.demo`). No sub-packages.
- **Test classes**: Mirror the main package structure and use a `Tests` suffix (e.g., `MyWeatherApplicationTests`).

### 3. Architecture Patterns

The project follows a layered architecture with clear separation of concerns:

- **Controller layer**: Annotated with `@RestController`, handles HTTP requests and delegates to services.
- **Service layer**: Annotated with `@Service`, contains business logic and external API calls.
- **Application class**: Annotated with `@SpringBootApplication`, serves as the entry point and contains `@Bean` definitions for shared infrastructure.

```java
// Controller -> Service delegation pattern
@RestController
public class MyWeatherController {

	@Autowired
	private MyWeatherService myWeatherService;

	@RequestMapping(name="/weather")
	public ResponseEntity<String> getWeather(@RequestParam("city") String city) {
		return myWeatherService.getWeather(city);
	}
}
```

```java
// Bean configuration in the application class
@SpringBootApplication
public class MyWeatherApplication {

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}
}
```

### 4. Formatting Rules

- **Indentation**: Tabs (not spaces).
- **Brace style**: Opening braces on the same line as the declaration.
- **Blank lines**: One blank line between class-level members (fields, methods).
- **Annotations**: Placed on separate lines above the annotated element.
- **Imports**: Grouped by top-level package (`java.*`, `org.slf4j.*`, `org.springframework.*`), with a blank line between groups.

Example demonstrating these rules:

```java
package com.example.demo;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MyWeatherService {

	Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private RestTemplate restTemplate;

	public ResponseEntity<String> getWeather(String city) {
		// method body
	}
}
```

### 5. Dependency Injection

- **Field injection** using `@Autowired` (not constructor injection):

```java
@Autowired
private MyWeatherService myWeatherService;

@Autowired
private RestTemplate restTemplate;
```

- **Bean definitions** are declared in the main application class (`MyWeatherApplication`) using `@Bean` methods.

### 6. Logging

- **Framework**: SLF4J via `LoggerFactory`.
- **Pattern**: Instance-level (non-static) logger initialized with `this.getClass()`:

```java
Logger logger = LoggerFactory.getLogger(this.getClass());
```

- **Usage**: Informational logging of constructed URLs and runtime state:

```java
logger.info("URL " + builder.toUriString());
```

### 7. REST API Conventions

- **Endpoint mapping**: `@RequestMapping` for defining endpoints.
- **Query parameters**: `@RequestParam` with explicit value names.
- **Return type**: `ResponseEntity<String>` for returning raw JSON responses.
- **CORS**: `@CrossOrigin` annotation available but commented out for development flexibility.

```java
@RequestMapping(name="/weather")
public ResponseEntity<String> getWeather(@RequestParam("city") String city) {
	return myWeatherService.getWeather(city);
}
```

### 8. Testing Conventions

- **Framework**: JUnit 4 with Spring test support.
- **Annotations**: `@RunWith(SpringRunner.class)` and `@SpringBootTest` for integration tests.
- **Class naming**: Follows `<ClassName>Tests` pattern (e.g., `MyWeatherApplicationTests`).

```java
@RunWith(SpringRunner.class)
@SpringBootTest
public class MyWeatherApplicationTests {

	@Test
	public void contextLoads() {
	}
}
```

### 9. Build and Dependency Management

- **Build tool**: Maven with Spring Boot parent POM (`spring-boot-starter-parent:2.1.2.RELEASE`) for dependency version management.
- **Wrapper**: Maven Wrapper (`mvnw` / `mvnw.cmd`) included for reproducible builds without requiring a pre-installed Maven.
- **Dependencies**: Managed via Spring Boot starters (`spring-boot-starter-web`, `spring-boot-starter-actuator`, `spring-boot-starter-test`).
- **Plugins**: Only `spring-boot-maven-plugin` configured. No explicit code formatting or static analysis plugins (Checkstyle, PMD, SpotBugs) are configured.
- **Java version**: 1.8 (set via `<java.version>` property in `pom.xml`).
