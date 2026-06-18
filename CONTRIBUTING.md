# Contributing to Weather Service

Thank you for considering contributing to this project! This guide outlines the conventions and standards we follow.

## Table of Contents

- [Getting Started](#getting-started)
- [Development Workflow](#development-workflow)
- [Code Style](#code-style)
- [Commit Messages](#commit-messages)
- [Pull Request Guidelines](#pull-request-guidelines)

## Getting Started

### Prerequisites

- Java 8 or higher
- Maven 3.6+
- An IDE with Java support (IntelliJ IDEA, Eclipse, or VS Code)

### Building the Project

```bash
./mvnw clean install
```

### Running Locally

```bash
./mvnw spring-boot:run
```

The application will start on `http://localhost:8080`.

## Development Workflow

1. Fork the repository and clone your fork.
2. Create a feature branch from `master`:
   ```bash
   git checkout -b feature/your-feature-name
   ```
3. Make your changes following the [Code Style](#code-style) guidelines.
4. Write or update tests as needed.
5. Ensure all tests pass: `./mvnw test`
6. Commit your changes following the [Commit Messages](#commit-messages) convention.
7. Push your branch and open a Pull Request.

## Code Style

### Java Conventions

- Follow the [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html) with the modifications noted below.
- Use **4 spaces** for indentation (no tabs).
- Maximum line length: **120 characters**.
- Always use braces for `if`, `for`, `while`, and `do` statements, even for single-line bodies.
- Prefer `final` for local variables that are not reassigned.
- Use descriptive, meaningful names for classes, methods, and variables.

### Naming Conventions

| Element       | Convention         | Example                     |
|---------------|--------------------|-----------------------------|
| Classes       | PascalCase         | `WeatherController`         |
| Methods       | camelCase          | `getWeatherByCity()`        |
| Variables     | camelCase          | `cityName`                  |
| Constants     | UPPER_SNAKE_CASE   | `MAX_RETRY_COUNT`           |
| Packages      | lowercase          | `com.example.weather`       |

### Spring Boot Conventions

- Use constructor injection over field injection (`@Autowired` on fields).
- Annotate service classes with `@Service` and controllers with `@RestController`.
- Use `@GetMapping`, `@PostMapping`, etc. instead of generic `@RequestMapping` where possible.
- Keep controllers thin: delegate business logic to service classes.
- Externalize configuration (API keys, URLs) using `application.properties` or environment variables. Never hardcode secrets.

### Logging

- Use SLF4J (`org.slf4j.Logger`) for all logging.
- Use parameterized messages instead of string concatenation:
  ```java
  // Good
  logger.info("Fetching weather for city: {}", city);

  // Avoid
  logger.info("Fetching weather for city: " + city);
  ```
- Log levels:
  - `ERROR` - Unexpected failures requiring immediate attention.
  - `WARN` - Potentially harmful situations.
  - `INFO` - High-level application flow (startup, requests served).
  - `DEBUG` - Detailed diagnostic information.

### Error Handling

- Use appropriate HTTP status codes in responses.
- Return structured error responses (not raw exception messages).
- Use `@ControllerAdvice` for global exception handling.
- Never swallow exceptions silently.

## Commit Messages

Follow the [Conventional Commits](https://www.conventionalcommits.org/) specification:

```
<type>(<scope>): <short summary>

<optional body>

<optional footer>
```

### Types

| Type       | Description                                      |
|------------|--------------------------------------------------|
| `feat`     | A new feature                                    |
| `fix`      | A bug fix                                        |
| `docs`     | Documentation-only changes                       |
| `style`    | Code style changes (formatting, no logic change) |
| `refactor` | Code change that neither fixes a bug nor adds a feature |
| `test`     | Adding or updating tests                         |
| `chore`    | Build process or auxiliary tool changes           |

### Examples

```
feat(weather): add support for 5-day forecast

fix(controller): handle missing city parameter gracefully

docs: add contributing guidelines
```

## Pull Request Guidelines

- Keep PRs focused and small (one concern per PR).
- Write a clear title following the commit message convention.
- Include a description explaining **what** changed and **why**.
- Reference related issues using `Closes #123` or `Fixes #123`.
- Ensure CI checks pass before requesting review.
- Respond to review feedback promptly.

### PR Checklist

- [ ] Code follows the project style guidelines
- [ ] Tests added/updated for the changes
- [ ] Documentation updated if needed
- [ ] No hardcoded secrets or sensitive data
- [ ] All existing tests pass
