package com.gcu.videogamestore.service;

import com.gcu.videogamestore.entity.User;
import com.gcu.videogamestore.model.Login;
import com.gcu.videogamestore.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Verifies database-backed credential business rules independently of MVC.
 */
class LoginServiceTests {

    private final UserRepository userRepository = mock(UserRepository.class);
    private final PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
    private final LoginService loginService = new LoginService(userRepository, passwordEncoder);

    @Test
    void authenticatesAStoredAccountWithoutUsernameCaseSensitivity() {
        User user = new User("gameforge", "stored-hash");
        when(userRepository.findByUsernameIgnoreCase("GameForge")).thenReturn(java.util.Optional.of(user));
        when(passwordEncoder.matches("GameForge123", "stored-hash")).thenReturn(true);

        assertTrue(loginService.authenticate(login(" GameForge ", "GameForge123")));
        assertEquals("GameForge", loginService.getAuthenticatedUsername(login(" GameForge ", "GameForge123")));
    }

    @Test
    void rejectsIncorrectOrIncompleteCredentials() {
        when(userRepository.findByUsernameIgnoreCase("gameforge"))
                .thenReturn(java.util.Optional.of(new User("gameforge", "stored-hash")));
        when(passwordEncoder.matches("wrong-password", "stored-hash")).thenReturn(false);

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