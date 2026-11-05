package com.gcu.videogamestore.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.gcu.videogamestore.data.entity.UserEntity;
import com.gcu.videogamestore.data.repository.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

class DatabaseUserDetailsServiceTests {

    private final UserRepository userRepository = mock(UserRepository.class);
    private final DatabaseUserDetailsService userDetailsService =
            new DatabaseUserDetailsService(userRepository);

    @Test
    void loadsRegisteredUserByUsername() {
        UserEntity user = new UserEntity(
                "Game",
                "Forge",
                "gameforge@test.com",
                "5551234567",
                "gameforge",
                "stored-password"
        );
        when(userRepository.findByUsername("gameforge")).thenReturn(Optional.of(user));

        UserDetails userDetails = userDetailsService.loadUserByUsername("gameforge");

        assertEquals("gameforge", userDetails.getUsername());
        assertEquals("stored-password", userDetails.getPassword());
        assertEquals(1, userDetails.getAuthorities().size());
        assertEquals("ROLE_USER", userDetails.getAuthorities().iterator().next().getAuthority());
        verify(userRepository).findByUsername("gameforge");
    }

    @Test
    void rejectsUnknownUsername() {
        when(userRepository.findByUsername("missing")).thenReturn(Optional.empty());

        assertThrows(
                UsernameNotFoundException.class,
                () -> userDetailsService.loadUserByUsername("missing")
        );
        verify(userRepository).findByUsername("missing");
    }
}