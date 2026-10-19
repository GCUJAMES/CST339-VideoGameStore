package com.gcu.videogamestore.service;

import com.gcu.videogamestore.model.Login;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Verifies the simulated credential business rules independently of MVC.
 */
class LoginServiceTests {

    private final LoginService loginService = new LoginService();

    @Test
    void authenticatesTheDemoAccountWithoutUsernameCaseSensitivity() {
        assertTrue(loginService.authenticate(login(" GameForge ", "GameForge123")));
        assertEquals("gameforge", loginService.getAuthenticatedUsername());
    }

    @Test
    void rejectsIncorrectOrIncompleteCredentials() {
        assertFalse(loginService.authenticate(login("gameforge", "wrong-password")));
        assertFalse(loginService.authenticate(login(null, "GameForge123")));
        assertFalse(loginService.authenticate(null));
    }

    private Login login(String username, String password) {
        Login login = new Login();
        login.setUsername(username);
        login.setPassword(password);
        return login;
    }
}