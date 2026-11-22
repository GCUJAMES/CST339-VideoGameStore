package com.gcu.videogamestore.business;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.gcu.videogamestore.data.entity.UserEntity;
import com.gcu.videogamestore.data.repository.UserRepository;
import com.gcu.videogamestore.model.UserModel;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

class RegistrationBusinessServiceTests {

    private final UserRepository userRepository = mock(UserRepository.class);
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final RegistrationBusinessService registrationService =
            new RegistrationBusinessService(userRepository, passwordEncoder);

    @Test
    void storesANonPlaintextPasswordHash() {
        UserModel user = validUser();
        when(userRepository.save(org.mockito.ArgumentMatchers.any(UserEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        assertTrue(registrationService.register(user));

        ArgumentCaptor<UserEntity> entityCaptor = ArgumentCaptor.forClass(UserEntity.class);
        verify(userRepository).save(entityCaptor.capture());

        UserEntity savedUser = entityCaptor.getValue();
        assertEquals("gameforge", savedUser.getUsername());
        assertNotEquals(user.getPassword(), savedUser.getPassword());
        assertTrue(passwordEncoder.matches(user.getPassword(), savedUser.getPassword()));
    }

    private UserModel validUser() {
        UserModel user = new UserModel();
        user.setFirstName("Game");
        user.setLastName("Forge");
        user.setEmailAddress("gameforge@test.com");
        user.setPhoneNumber("5551234567");
        user.setUsername("gameforge");
        user.setPassword("GameForge12");
        return user;
    }
}
