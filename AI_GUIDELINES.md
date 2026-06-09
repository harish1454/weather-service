# AI Guidelines for Weather Service

This document provides guidelines for AI assistants working with this codebase.

## Project Overview

- **Type**: Java/Spring Boot web application
- **Build Tool**: Maven (with Maven Wrapper - `mvnw`)
- **Java Version**: 1.8
- **Spring Boot Version**: 2.1.2.RELEASE
- **Purpose**: A weather service demo application

## Project Structure

```
weather-service/
├── src/
│   ├── main/
│   │   ├── java/com/example/demo/
│   │   │   ├── MyWeatherApplication.java    # Spring Boot entry point
│   │   │   ├── MyWeatherController.java     # REST controller
│   │   │   └── MyWeatherService.java        # Business logic service
│   │   └── resources/
│   │       ├── application.properties       # Application configuration
│   │       └── static/
│   │           └── index.html               # Welcome page
│   └── test/
│       └── java/com/example/demo/
│           └── MyWeatherApplicationTests.java  # Test class
├── pom.xml                                  # Maven build configuration
├── mvnw / mvnw.cmd                          # Maven wrapper scripts
└── README.md
```

## Build and Run Commands

| Action | Command |
|--------|---------|
| Build the project | `./mvnw clean package` |
| Run tests | `./mvnw test` |
| Run the application | `./mvnw spring-boot:run` |
| Skip tests during build | `./mvnw clean package -DskipTests` |

## Coding Conventions

### General

- Follow standard Java naming conventions (camelCase for methods/variables, PascalCase for classes)
- Use the `com.example.demo` package structure
- Keep controller, service, and application layers separated
- Use Spring annotations (`@RestController`, `@Service`, `@SpringBootApplication`)

### REST API

- Controllers should be annotated with `@RestController`
- Use appropriate HTTP method annotations (`@GetMapping`, `@PostMapping`, etc.)
- Keep controller methods focused and delegate business logic to service classes

### Testing

- Place test classes under `src/test/java` with the same package structure as main sources
- Use Spring Boot's test annotations (`@SpringBootTest`, `@RunWith(SpringRunner.class)`)
- Ensure all tests pass before submitting changes

### Dependencies

- Dependencies are managed through the parent Spring Boot BOM
- Add new dependencies to `pom.xml` under the `<dependencies>` section
- Prefer Spring Boot starters when available

## Key Considerations

1. **Compatibility**: This project uses Java 8. Do not use features from Java 9+.
2. **Spring Boot version**: The project uses Spring Boot 2.1.x. Be aware of API differences compared to newer versions.
3. **Maven Wrapper**: Always use `./mvnw` (or `mvnw.cmd` on Windows) instead of a system-installed Maven to ensure consistent builds.
4. **Static resources**: Static web content goes in `src/main/resources/static/`.
5. **Configuration**: Application properties are managed in `src/main/resources/application.properties`.

## Contribution Workflow

1. Create a feature branch from `master`
2. Make your changes following the conventions above
3. Run `./mvnw clean test` to ensure all tests pass
4. Submit a pull request with a clear description of the changes
