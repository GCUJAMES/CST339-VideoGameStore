package com.gcu.videogamestore.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.gcu.videogamestore.model.UserModel;

import jakarta.validation.Valid;

@Controller
public class RegisterController {

    @GetMapping("/register")
    public String register(Model model) {
    	model.addAttribute("userModel", new UserModel());
        return "register";
    }
    
    @PostMapping("/doRegister")
	public String doRegister(@Valid UserModel userModel, BindingResult bindingResult, Model model)
	{
		// Check for validation errors
		if (bindingResult.hasErrors())
		{
			model.addAttribute("title", "Login Form");
			return "register";
		}
		
		return "home";
	}
}