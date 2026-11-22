# Login and Registration Module — Class Diagram

This diagram documents the final database-backed authentication design. New passwords are encoded with BCrypt before persistence, and the same stored hash supports form login and HTTP Basic authentication for the service API.

```mermaid
classDiagram
    class LoginController {
        +showLoginForm(Model model) String
    }

    class SecurityConfig {
        +passwordEncoder() PasswordEncoder
        +securityFilterChain(HttpSecurity, DatabaseUserDetailsService) SecurityFilterChain
    }

    class RegistrationBusinessService {
        -UserRepository userRepository
        -PasswordEncoder passwordEncoder
        +RegistrationBusinessService(UserRepository, PasswordEncoder)
        +register(UserModel user) boolean
        +usernameExists(String username) boolean
        +emailExists(String emailAddress) boolean
    }

    class DatabaseUserDetailsService {
        -UserRepository userRepository
        +DatabaseUserDetailsService(UserRepository)
        +loadUserByUsername(String username) UserDetails
    }

    class UserRepository {
        <<interface>>
        +existsByUsername(String username) boolean
        +existsByEmailAddress(String emailAddress) boolean
        +findByUsername(String username) Optional~UserEntity~
    }

    class UserEntity {
        -Long id
        -String firstName
        -String lastName
        -String emailAddress
        -String phoneNumber
        -String username
        -String password
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

    LoginController ..> Login : binds form data
    SecurityConfig --> DatabaseUserDetailsService : configures authentication
    SecurityConfig --> PasswordEncoder : creates BCrypt encoder
    RegistrationBusinessService --> UserRepository : constructor injection
    RegistrationBusinessService --> PasswordEncoder : encodes before save
    DatabaseUserDetailsService --> UserRepository : loads Basic auth user
    UserRepository --> UserEntity : persists and retrieves
```

## Relationship summary

- `LoginController` displays the custom login page; Spring Security processes submitted credentials and logout requests.
- `SecurityConfig` protects routes, configures form login and HTTP Basic authentication, enforces CSRF on browser requests, and creates the BCrypt encoder.
- `RegistrationBusinessService` checks uniqueness and stores only the BCrypt-encoded password.
- `DatabaseUserDetailsService` supplies the same account data for browser form login and HTTP Basic authentication on `/service/**`.
- `UserRepository` is a Spring Data JDBC `CrudRepository` backed by MySQL.
- `UserEntity.password` contains the BCrypt hash despite retaining the existing database column/property name.
- `Login` is the validated form model carrying the submitted username and password.

Existing rows created before BCrypt was enabled must have their passwords reset or be re-registered; plaintext fallback authentication is intentionally not supported.
