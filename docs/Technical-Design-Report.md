# GameForge Technical Design and Configuration Report

## System overview

GameForge is a Java 17 Spring Boot web application for managing and viewing a video-game catalog. It combines server-rendered Thymeleaf pages with a read-only REST service that returns the catalog as JSON or XML.

## Architecture

The application uses a layered design:

1. **Presentation layer** — Spring MVC controllers and Thymeleaf templates handle home, registration, login, and product-management flows. `GameRestController` exposes catalog data to API clients.
2. **Service/business layer** — `ProductService`, `LoginService`, and `RegistrationBusinessService` coordinate application rules.
3. **Data layer** — `GameDataService` uses `JdbcTemplate` for catalog CRUD, while `UserRepository` uses Spring Data JDBC for account persistence.
4. **Database layer** — MySQL stores users, games, platforms, and game-to-platform relationships.

Dependencies are supplied through constructor injection. `SpringConfigRegistration` exposes the registration implementation through its interface.

## Data model

- `users` stores registration and authentication data represented by `UserEntity`.
- `games` stores title, price, release date, description, and category data.
- `platforms` stores platform names.
- `game_platforms` implements the many-to-many relationship between games and platforms.

`GameDataService.getAllGames()` retrieves the catalog and all platform mappings with two queries. This avoids issuing one additional platform query for every game.

## Authentication and authorization

- New account passwords are encoded with BCrypt before `UserEntity` is saved.
- Browser login uses `LoginService` and `PasswordEncoder.matches`.
- Successful browser login stores `loggedIn` and `username` in the HTTP session.
- `/service/**` is protected by Spring Security HTTP Basic authentication.
- `DatabaseUserDetailsService` supplies registered users to Spring Security.
- Other routes remain publicly reachable at the Spring Security filter level; product controllers enforce the existing browser-session checks.
- CSRF is ignored only for `/service/**`; normal MVC routes retain CSRF protection.

Accounts created before BCrypt support contain incompatible plaintext values. Those users must reset their password or register again; there is no plaintext fallback.

## Runtime configuration

The application reads these properties from `src/main/resources/application.properties`:

| Environment variable | Development default | Purpose |
|---|---|---|
| `DB_URL` | `jdbc:mysql://localhost:3306/gameforge` | JDBC connection URL |
| `DB_USERNAME` | `root` | MySQL username |
| `DB_PASSWORD` | `root` | Local MySQL password |

The MySQL JDBC driver is `com.mysql.cj.jdbc.Driver`. SQL initialization is disabled with `spring.sql.init.mode=never`, so the schema must exist before startup. Deployment environments should override all database values and must not commit production credentials.

## Service API

The read-only service exposes:

- `GET /service/getjson` — returns all games as `application/json`.
- `GET /service/getxml` — returns all games as `application/xml`.

Both endpoints require HTTP Basic credentials for a registered BCrypt-backed user. The complete contract is in `Service-API.md`.

## Build and test strategy

The Maven wrapper provides reproducible build commands. Automated coverage includes:

- MVC login and logout behavior
- BCrypt registration and login verification
- REST endpoint authentication and response media types
- Product controller/service behavior
- Catalog platform loading and delete behavior
- Spring application context startup

A final clean package should be run before submission. Generated `target/` content is excluded by `.gitignore` and should not be committed or included as source documentation.

## Final maintenance notes

- Authoritative source is under `src/main` and `src/test`; `target/`, `bin/`, and IDE metadata are generated artifacts.
- The current authentication diagram is `Login-Module-Class-Diagram.md` in this directory.
- The earlier milestone-level simulated-login diagram is marked historical and is not the final design.
