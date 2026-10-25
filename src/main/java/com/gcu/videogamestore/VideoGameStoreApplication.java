package com.gcu.videogamestore;

import com.gcu.videogamestore.entity.User;
import com.gcu.videogamestore.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class VideoGameStoreApplication {

	public static void main(String[] args) {
		SpringApplication.run(VideoGameStoreApplication.class, args);
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	CommandLineRunner seedUsers(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		return args -> {
			if (userRepository.findByUsernameIgnoreCase("gameforge").isEmpty()) {
				userRepository.save(new User("gameforge", passwordEncoder.encode("GameForge123")));
			}
		};
	}
}
