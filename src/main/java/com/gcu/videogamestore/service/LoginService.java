package com.gcu.videogamestore.service;

import com.gcu.videogamestore.entity.User;
import com.gcu.videogamestore.model.Login;
import com.gcu.videogamestore.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Encapsulates database-backed login business rules.
 */
@Service
public class LoginService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public LoginService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Determines whether submitted credentials match a stored user.
     *
     * @param login submitted credentials
     * @return true when a matching stored user has the supplied password
     */
    public boolean authenticate(Login login) {
        if (login == null || login.getUsername() == null || login.getPassword() == null) {
            return false;
        }

        return userRepository.findByUsernameIgnoreCase(login.getUsername().trim())
                .map(User::getPasswordHash)
                .map(storedPasswordHash -> passwordEncoder.matches(login.getPassword(), storedPasswordHash))
                .orElse(false);
    }

    /**
     * Normalizes the authenticated username for storage in the browser session.
     *
     * @param login validated login credentials
     * @return the username that was authenticated
     */
    public String getAuthenticatedUsername(Login login) {
        return login.getUsername().trim();
    }
}
