package com.gcu.videogamestore.business;

import com.gcu.videogamestore.model.UserModel;

public class RegistrationBusinessService implements RegistrationBusinessInterface
{
	@Override
	public boolean register(UserModel user)
	{
		System.out.println("Registering user: " + user.getUsername());
		return true;
	}
}