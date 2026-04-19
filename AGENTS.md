# Repository Guidelines

## Project Structure & Module Organization
This repository is a Java 21 Spring Boot application with a small static web frontend.

- `src/main/java/edu/pingme/messaging_stomp_websocket/`: application code, controllers, config, and utility classes.
- `src/main/resources/`: runtime config such as `application.properties`.
- `src/main/resources/static/`: browser assets served by Spring Boot (`index.html`, `app.js`, icons, helper pages).
- `src/test/java/edu/pingme/messaging_stomp_websocket/`: JUnit 5 tests.
- `docs/`: GitHub Pages and documentation assets.

Keep new Java classes inside the existing package unless there is a clear reason to split modules.

## Build, Test, and Development Commands
- `./gradlew bootRun` or `gradlew.bat bootRun`: run the app locally with Gradle.
- `./gradlew test` or `gradlew.bat test`: run the JUnit test suite.
- `./gradlew build`: compile, test, and package with Gradle.
- `./mvnw spring-boot:run`: alternate local run path for Maven users.
- `./mvnw test`: run tests through Maven.

Use the wrapper scripts already committed to the repo instead of a globally installed Gradle or Maven.

## Coding Style & Naming Conventions
Follow the existing code style:

- Java uses tabs in some older files and 4-space indentation in others; preserve the surrounding file’s style instead of reformatting unrelated lines.
- Class names use `PascalCase`; methods and fields use `camelCase`.
- Test classes end with `Test` and mirror the class or behavior under test, for example `UtilsTest`.
- Keep controllers and config classes small; move reusable logic into utility or domain classes.

## Testing Guidelines
Tests use JUnit 5 with `spring-boot-starter-test`. Prefer focused unit tests for helpers such as `Utils`, and only use Spring context tests when wiring matters.

- Put tests under `src/test/java/...`.
- Name test methods by behavior, for example `testEncryptedAndBase64`.
- Run `./gradlew test` before opening a PR.

There is no stated coverage gate, so aim to cover changed logic and edge cases.

## Commit & Pull Request Guidelines
Recent commits use short, direct subjects such as `Update emojies.html. Typo` and `Supports navigator.geolocation`. Follow that pattern: one-line, present-tense summaries focused on the change.

PRs should include:

- a short description of the behavior change,
- linked issue or context when relevant,
- screenshots for changes under `src/main/resources/static/` or `docs/`,
- notes about config changes such as `application.properties`, ChromeDriver, or ngrok setup.
