package com.gcu.videogamestore;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.gcu.videogamestore.business.RegistrationBusinessInterface;
import com.gcu.videogamestore.business.RegistrationBusinessService;

@Configuration
public class SpringConfigRegistration
{
	@Bean(name="registrationBusinessService")
	public RegistrationBusinessInterface getRegistrationBusiness()
	{
		return new RegistrationBusinessService();
	}
}