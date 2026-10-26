package com.gcu.videogamestore.service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;

import com.gcu.videogamestore.data.entity.UserEntity;
import com.gcu.videogamestore.data.repository.UserRepository;
import com.gcu.videogamestore.model.Login;

/**
 * Tests the database-backed login service.
 */
class LoginServiceTests {

    private final UserRepository userRepository =
            mock(UserRepository.class);

    private final LoginService loginService =
            new LoginService(userRepository);

    @Test
    void authenticatesStoredUser() {

        UserEntity user = new UserEntity(
                "Game",
                "Forge",
                "gameforge@test.com",
                "5551234567",
                "gameforge",
                "GameForge123"
        );

        when(userRepository.findByUsername("gameforge"))
                .thenReturn(Optional.of(user));

        assertTrue(
                loginService.authenticate(
                        login("gameforge", "GameForge123")
                )
        );
    }

    @Test
    void rejectsIncorrectPassword() {

        UserEntity user = new UserEntity(
                "Game",
                "Forge",
                "gameforge@test.com",
                "5551234567",
                "gameforge",
                "GameForge123"
        );

        when(userRepository.findByUsername("gameforge"))
                .thenReturn(Optional.of(user));

        assertFalse(
                loginService.authenticate(
                        login("gameforge", "wrong-password")
                )
        );
    }

    @Test
    void rejectsMissingUser() {

        when(userRepository.findByUsername("missing"))
                .thenReturn(Optional.empty());

        assertFalse(
                loginService.authenticate(
                        login("missing", "password")
                )
        );
    }

    @Test
    void rejectsIncompleteCredentials() {

        assertFalse(
                loginService.authenticate(
                        login(null, "GameForge123")
                )
        );

        assertFalse(
                loginService.authenticate(
                        login("gameforge", null)
                )
        );

        assertFalse(loginService.authenticate(null));
    }

    private Login login(String username, String password) {

        Login login = new Login();

        login.setUsername(username);
        login.setPassword(password);

        return login;
    }
}