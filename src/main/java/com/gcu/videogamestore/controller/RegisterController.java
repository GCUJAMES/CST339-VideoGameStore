package com.gcu.videogamestore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.gcu.videogamestore.business.RegistrationBusinessInterface;
import com.gcu.videogamestore.model.UserModel;

import jakarta.validation.Valid;

@Controller
public class RegisterController
{
	@Autowired
	private RegistrationBusinessInterface service;

	@GetMapping("/register")
	public String register(Model model)
	{
		model.addAttribute("title", "Register");
		model.addAttribute("userModel", new UserModel());
		return "register";
	}

	@PostMapping("/doRegister")
	public String doRegister(@Valid UserModel userModel, BindingResult bindingResult, Model model)
	{
		if (bindingResult.hasErrors())
		{
			model.addAttribute("title", "Register");
			return "register";
		}

		// duplicate checks before save
		if (service.usernameExists(userModel.getUsername()))
		{
			bindingResult.rejectValue("username", "duplicate", "Username is already taken");
		}
		if (service.emailExists(userModel.getEmailAddress()))
		{
			bindingResult.rejectValue("emailAddress", "duplicate", "Email is already registered");
		}
		if (bindingResult.hasErrors())
		{
			model.addAttribute("title", "Register");
			return "register";
		}

		if (!service.register(userModel))
		{
			model.addAttribute("title", "Register");
			model.addAttribute("error", "Registration failed. Please try again.");
			return "register";
		}

		return "home";
	}
}