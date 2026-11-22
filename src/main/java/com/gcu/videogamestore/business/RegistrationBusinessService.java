package com.gcu.videogamestore.business;

import org.springframework.dao.DuplicateKeyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.gcu.videogamestore.data.entity.UserEntity;
import com.gcu.videogamestore.data.repository.UserRepository;
import com.gcu.videogamestore.model.UserModel;

public class RegistrationBusinessService implements RegistrationBusinessInterface
{
	private static final Logger logger = LoggerFactory.getLogger(RegistrationBusinessService.class);

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	public RegistrationBusinessService(UserRepository userRepository, PasswordEncoder passwordEncoder)
	{
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public boolean register(UserModel user)
	{
		if (userRepository.existsByUsername(user.getUsername()) ||
			userRepository.existsByEmailAddress(user.getEmailAddress()))
		{
			return false;
		}

		try
		{
			UserEntity entity = new UserEntity(
				user.getFirstName(),
				user.getLastName(),
				user.getEmailAddress(),
				user.getPhoneNumber(),
				user.getUsername(),
				passwordEncoder.encode(user.getPassword())
			);

			userRepository.save(entity);
			return true;
		}
		catch (DuplicateKeyException e)
		{
			return false;
		}
		catch (Exception e)
		{
			logger.error("Unable to register user {}", user.getUsername(), e);
			return false;
		}
	}
	
	@Override
	public boolean usernameExists(String username)
	{
		return userRepository.existsByUsername(username);
	}

	@Override
	public boolean emailExists(String emailAddress)
	{
		return userRepository.existsByEmailAddress(emailAddress);
	}
}