package com.gcu.videogamestore.repository;

import com.gcu.videogamestore.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Verifies the login data-access layer against the embedded H2 database.
 */
@DataJpaTest
class UserRepositoryTests {

    @Autowired
    private UserRepository userRepository;

    @Test
    void findsAStoredUserWithoutUsernameCaseSensitivity() {
        userRepository.save(new User("repository-test-user", "hashed-password"));

        User user = userRepository.findByUsernameIgnoreCase("REPOSITORY-TEST-USER").orElseThrow();

        assertEquals("repository-test-user", user.getUsername());
        assertEquals("hashed-password", user.getPasswordHash());
        assertTrue(user.getId() > 0);
    }
}