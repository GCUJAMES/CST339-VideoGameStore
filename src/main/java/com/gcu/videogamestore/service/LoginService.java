package com.gcu.videogamestore.service;

import com.gcu.videogamestore.model.Login;
import org.springframework.stereotype.Service;

/**
 * Encapsulates the simulated login business rules for the current milestone.
 * Database-backed authentication is intentionally deferred to a later milestone.
 */
@Service
public class LoginService {

    private static final String DEMO_USERNAME = "gameforge";
    private static final String DEMO_PASSWORD = "GameForge123";

    /**
     * Determines whether the submitted credentials match the simulated account.
     *
     * @param login submitted credentials
     * @return true when the credentials represent the simulated GameForge account
     */
    public boolean authenticate(Login login) {
        return login != null
                && login.getUsername() != null
                && login.getPassword() != null
                && DEMO_USERNAME.equalsIgnoreCase(login.getUsername().trim())
                && DEMO_PASSWORD.equals(login.getPassword());
    }

    /**
     * Returns the username stored in session after a successful simulated login.
     *
     * @return the simulated account username
     */
    public String getAuthenticatedUsername() {
        return DEMO_USERNAME;
    }
}
