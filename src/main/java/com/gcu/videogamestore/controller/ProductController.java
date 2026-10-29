package com.gcu.videogamestore.controller;

import com.gcu.videogamestore.model.Product;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import com.gcu.videogamestore.service.ProductService;


 // Handles product-related pages for the GameForge application.
@Controller
public class ProductController {
	
	// Used for product business logic
	private final ProductService productService;
	
	// Constructor
	public ProductController(ProductService productService) {
	    this.productService = productService;
	}
 
     // Displays the product landing page after the user logs in.
    @GetMapping("/products")
    public String showProducts(HttpSession session, Model model) {

        // Prevent access if the user is not logged in
        if (!Boolean.TRUE.equals(session.getAttribute("loggedIn"))) {
            return "redirect:/login";
        }

        // Send the user name to the products page
        model.addAttribute("username", session.getAttribute("username"));
        
        // Gets all games from the database
        model.addAttribute("products", productService.getAllGames());

        return "products";
    }

    
    // Displays the Create Product form.    
    @GetMapping("/products/create")
    public String showCreateProduct(HttpSession session, Model model) {

        // Prevent access if the user is not logged in
        if (!Boolean.TRUE.equals(session.getAttribute("loggedIn"))) {
            return "redirect:/login";
        }

        // Create an empty Product object for the form
        model.addAttribute("product", new Product());

        return "create-product";
    }

    
    // Processes the Create Product form and validates user input.
    @PostMapping("/products/create")
    public String createProduct(
            @Valid Product product,
            BindingResult bindingResult,
            HttpSession session,
            Model model) {

        // Prevent access if the user is not logged in
        if (!Boolean.TRUE.equals(session.getAttribute("loggedIn"))) {
            return "redirect:/login";
        }

        // Return to the form if validation errors are found
        if (bindingResult.hasErrors()) {
            return "create-product";
        }
        
	     // Saves the product to the database
	     boolean created = productService.createProduct(product);
	     
	     // Shows an error if the product was not saved
	     if (!created) {
	         model.addAttribute(
	                 "errorMessage",
	                 "The game could not be added. Please try again."
	         );

	         return "create-product";
	     }
        
        // Clears the form
        model.addAttribute("product", new Product());

        // Success message 
        model.addAttribute("successMessage",
                "Game added successfully.");

        return "create-product";
    }
}