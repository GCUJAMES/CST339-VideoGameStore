package com.gcu.videogamestore;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.gcu.videogamestore.business.RegistrationBusinessInterface;
import com.gcu.videogamestore.business.RegistrationBusinessService;
import com.gcu.videogamestore.data.repository.UserRepository;

@Configuration
public class SpringConfigRegistration
{
	@Bean(name="registrationBusinessService")
	public RegistrationBusinessInterface getRegistrationBusiness(
		UserRepository userRepository,
		PasswordEncoder passwordEncoder)
	{
		return new RegistrationBusinessService(userRepository, passwordEncoder);
	}
}