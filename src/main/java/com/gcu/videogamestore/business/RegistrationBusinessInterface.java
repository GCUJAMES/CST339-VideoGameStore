package com.gcu.videogamestore.business;

import com.gcu.videogamestore.model.UserModel;

public interface RegistrationBusinessInterface
{
	boolean register(UserModel user);
	boolean usernameExists(String username);
	boolean emailExists(String emailAddress);
}