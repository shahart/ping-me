## Purpose
This file guides AI coding agents to be productive in the `ping-me` repository quickly. Focus on the Spring Boot backend, the small static frontend, and the Selenium-driven automation flow described in `README.md`.

## Big picture
- Backend: Spring Boot app under `src/main/java/edu.pingme` (artifact `messaging-stomp-websocket`).
- Frontend: static pages and client JS live in `src/main/resources/static` and server templates in `src/main/resources/templates`.
- Automation: Selenium + Chrome remote debugging is used to drive WhatsApp web. See `README.md` for the Chrome-for-testing download and `ngrok` usage.
- Packaging: Project uses Java 21 and is packaged as a `war` (see `pom.xml`). `spring-boot-starter-tomcat` is `provided` scope, so it can run standalone via Spring Boot or be deployed to a servlet container.

## Key files and directories
- `src/main/java/edu/pingme/` — application code and controllers.
- `src/main/resources/application.properties` — runtime config (default password noted in `README.md`, `1234`).
- `src/main/resources/static/` — static UI (`index.html`, `emojies.html`, `app.js`).
- `pom.xml` and `build.gradle` — both present; project uses Java 21 and Spring Boot 3.5.3.
- `README.md` — contains essential run/debug steps (ngrok, Chrome remote debugging, WhatsApp setup).

## Build / Run / Test (explicit commands)
- Preferred local build (Gradle wrapper):
  - `./gradlew build`
  - `./gradlew bootRun` (run locally)
  - `./gradlew test`
- Maven wrapper alternatives (present in repo):
  - `./mvnw package`
  - `./mvnw spring-boot:run -Dspring-boot.run.arguments=--recipient.target=Elvis`
- Producing an executable artifact:
  - `./gradlew bootJar` or `./gradlew build` then `java -jar build/libs/*` (or use `target/` artifacts if built with Maven).

## Developer workflow highlights
- End-to-end automation relies on Chrome remote debugging and a matching ChromeDriver — README provides the exact Chrome-for-testing link. Tests may require Chrome/Chromedriver present and a running debugging Chrome instance.
- To expose your local server for watch testing, `ngrok http 8080` is used (README example). This is central to reproducing the watch-based usage flow.

## Conventions & patterns
- Packages live under `edu.pingme`. Tests follow standard `*Tests` naming (JUnit Platform). `test` tasks are configured to use JUnit Platform in `build.gradle`.
- Static assets are edited in `src/main/resources/static`. When changing UI, update these files and rerun the server (no separate frontend build pipeline).
- WebSocket-related libraries are present/mentioned but commented in `pom.xml` — review that file if you plan to enable WebSocket/STOMP features.

## External integrations
- WhatsApp web (driven by Selenium) — requires Chrome with `--remote-debugging-port` and ChromeDriver. See `README.md` for the exact `chrome-for-testing` download link and example `chrome` command.
- `ngrok` used to expose local endpoints to an iWatch browser for manual testing.

## Small editing examples for agents
- If updating the emoji page, edit `src/main/resources/static/emojies.html` (example: the document header indicates the emoji version `v17.0`).
- To add a configuration option, prefer `application.properties` and document the default in `README.md`.

## When you need more info
- If a task requires runtime credentials, ask the maintainer; the repo contains a default `1234` password reference but sensitive values should not be assumed.
- For build or test failures, check both `target/` and `build/` outputs and consult `README.md` notes about Chrome/Power settings on Windows.

