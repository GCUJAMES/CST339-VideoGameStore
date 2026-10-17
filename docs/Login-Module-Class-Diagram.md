# Milestone 3 — Login Module Class Diagram

This diagram documents the Login Module after the Spring Beans, Spring Core, and dependency-injection refactor. Authentication remains simulated; no database is used in this milestone.

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
        -String DEMO_USERNAME
        -String DEMO_PASSWORD
        +authenticate(Login login) boolean
        +getAuthenticatedUsername() String
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
    LoginService ..> Login : validates credentials
```

## Relationship summary

- `LoginController` is the presentation-layer Spring `@Controller`. It receives `LoginService` through constructor injection and retains validation, session management, redirects, and logout handling.
- `LoginService` is the business-layer Spring `@Service` bean. It contains only the simulated authentication business rule.
- `Login` is the validated form model carrying the submitted username and password.
