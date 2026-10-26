package com.gcu.videogamestore.service;

import org.springframework.stereotype.Service;

import com.gcu.videogamestore.data.entity.UserEntity;
import com.gcu.videogamestore.data.repository.UserRepository;
import com.gcu.videogamestore.model.Login;

@Service
public class LoginService {

    // Used to access users from the database
    private final UserRepository userRepository;

    public LoginService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Checks the username and password against the database
    public boolean authenticate(Login login) {

        if (login == null ||
                login.getUsername() == null ||
                login.getPassword() == null) {

            return false;
        }

        // Looks for the username in the database
        UserEntity user = userRepository
                .findByUsername(login.getUsername().trim())
                .orElse(null);

        if (user == null) {
            return false;
        }

        // Checks if the entered password matches
        return user.getPassword().equals(login.getPassword());
    }

    // Returns the username for the current session
    public String getAuthenticatedUsername(Login login) {
        return login.getUsername().trim();
    }
}
