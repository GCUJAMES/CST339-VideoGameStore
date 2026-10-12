package com.gcu.videogamestore.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Captures the credentials entered on the simulated Milestone 2 login form.
 * Authentication is intentionally in-memory until the database milestone.
 */
public class Login {

    @NotBlank(message = "Username is required.")
    @Size(max = 50, message = "Username must be 50 characters or fewer.")
    private String username;

    @NotBlank(message = "Password is required.")
    @Size(max = 100, message = "Password must be 100 characters or fewer.")
    private String password;

    /**
     * Gets the entered username.
     *
     * @return the username supplied by the user
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the entered username.
     *
     * @param username the username supplied by the user
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets the entered password.
     *
     * @return the password supplied by the user
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the entered password.
     *
     * @param password the password supplied by the user
     */
    public void setPassword(String password) {
        this.password = password;
    }
}