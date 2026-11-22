package com.gcu.videogamestore;

import com.gcu.videogamestore.service.DatabaseUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Applies the existing database-backed user lookup to protected REST endpoints.
 */
@Configuration
public class SecurityConfig {

    /**
     * Hashes stored passwords and verifies credentials without exposing plaintext values.
     */
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            DatabaseUserDetailsService databaseUserDetailsService) throws Exception {
        return http
                .userDetailsService(databaseUserDetailsService)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/service/**").authenticated()
                        .anyRequest().permitAll())
                .httpBasic(Customizer.withDefaults())
                .csrf(csrf -> csrf.ignoringRequestMatchers("/service/**"))
                .build();
    }
}