package com.gcu.videogamestore;

import com.gcu.videogamestore.service.DatabaseUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Applies the existing database-backed user lookup to protected REST endpoints.
 */
@Configuration
public class SecurityConfig {

        /**
         * Keeps the existing database password format while Basic authentication is added.
         */
        @Bean
        PasswordEncoder passwordEncoder() {
                return new PasswordEncoder() {
                        @Override
                        public String encode(CharSequence rawPassword) {
                                return rawPassword.toString();
                        }

                        @Override
                        public boolean matches(CharSequence rawPassword, String encodedPassword) {
                                return rawPassword.toString().equals(encodedPassword);
                        }
                };
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