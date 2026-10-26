package com.gcu.videogamestore.business;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;

import com.gcu.videogamestore.data.entity.UserEntity;
import com.gcu.videogamestore.data.repository.UserRepository;
import com.gcu.videogamestore.model.UserModel;

public class RegistrationBusinessService implements RegistrationBusinessInterface
{
	@Autowired
	private UserRepository userRepository;

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
				user.getPassword()
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
			e.printStackTrace();
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