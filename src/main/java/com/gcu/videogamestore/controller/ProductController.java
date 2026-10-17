package com.gcu.videogamestore.controller;

import com.gcu.videogamestore.model.Product;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * Handles product-related pages for the GameForge application.
 */
@Controller
public class ProductController {

    /**
     * Displays the product landing page after the user logs in.
     */
    @GetMapping("/products")
    public String showProducts(HttpSession session, Model model) {

        // Prevent access if the user is not logged in
        if (!Boolean.TRUE.equals(session.getAttribute("loggedIn"))) {
            return "redirect:/login";
        }

        // Send the username to the products page
        model.addAttribute("username", session.getAttribute("username"));

        return "products";
    }

    /**
     * Displays the Create Product form.
     */
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

    /**
     * Processes the Create Product form and validates user input.
     */
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
        
        // Clears the form
        model.addAttribute("product", new Product());

        // Database saving is not required for this milestone
        model.addAttribute("successMessage",
                "Product created successfully. Database persistence will be added in a later milestone.");

        return "create-product";
    }
}