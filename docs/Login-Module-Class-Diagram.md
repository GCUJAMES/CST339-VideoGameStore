# Login Database Refactor — Class Diagram

This diagram documents the Login Module after authentication moved from hardcoded credentials to an injected database data layer.

```mermaid
classDiagram
    class LoginController {
        -LoginService loginService
        +LoginController(LoginService loginService)
        +showLoginForm(Model model) String
        +processLogin(Login login, BindingResult bindingResult, HttpSession session) String
        +logout(HttpSession session) String
    }

    class LoginService {
        -UserRepository userRepository
        -PasswordEncoder passwordEncoder
        +LoginService(UserRepository, PasswordEncoder)
        +authenticate(Login login) boolean
        +getAuthenticatedUsername(Login login) String
    }

    class UserRepository {
        <<interface>>
        +findByUsernameIgnoreCase(String username) Optional~User~
    }

    class User {
        -Long id
        -String username
        -String passwordHash
    }

    class PasswordEncoder {
        <<interface>>
        +encode(String password) String
        +matches(String rawPassword, String encodedPassword) boolean
    }

    class Login {
        -String username
        -String password
        +getUsername() String
        +setUsername(String username) void
        +getPassword() String
        +setPassword(String password) void
    }

    LoginController --> LoginService : constructor injection
    LoginController ..> Login : binds form data
    LoginService --> UserRepository : constructor injection
    LoginService --> PasswordEncoder : constructor injection
    UserRepository --> User : retrieves
    LoginService ..> Login : validates credentials
```

## Relationship summary

- `LoginController` is the presentation-layer Spring `@Controller`. It receives `LoginService` through constructor injection and retains validation, session management, redirects, and logout handling.
- `LoginService` is the business-layer Spring `@Service` bean. It receives both the `UserRepository` and `PasswordEncoder` through constructor injection, so it contains no hardcoded credentials.
- `UserRepository` is the Spring Data JPA data-access layer that retrieves a persistent `User` by username.
- `User` stores a unique username and BCrypt password hash in the embedded H2 database. The application seeds the developer account at startup so the existing login flow remains demonstrable.
- `Login` is the validated form model carrying the submitted username and password.
