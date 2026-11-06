package com.gcu.videogamestore.controller;

import java.security.Principal;

import com.gcu.videogamestore.model.Product;
import com.gcu.videogamestore.service.ProductService;

import jakarta.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    public String showProducts(Principal principal, Model model) {

        // Send the authenticated user's name to the products page
        model.addAttribute("username", principal.getName());

        // Get all games from the database
        model.addAttribute("products", productService.getAllGames());

        return "products";
    }

    // Displays the Create Product form.
    @GetMapping("/products/create")
    public String showCreateProduct(Model model) {

        // Create an empty Product object for the form
        model.addAttribute("product", new Product());

        return "create-product";
    }

    // Processes the Create Product form and validates user input.
    @PostMapping("/products/create")
    public String createProduct(
            @Valid Product product,
            BindingResult bindingResult,
            Model model) {

        // Return to the form if validation errors are found
        if (bindingResult.hasErrors()) {
            return "create-product";
        }

        // Save the product to the database
        boolean created = productService.createProduct(product);

        // Show an error if the product failed to save
        if (!created) {
            model.addAttribute(
                    "errorMessage",
                    "The game could not be added. Please try again."
            );
            return "create-product";
        }

        // Clear the form and show a success message
        model.addAttribute("product", new Product());
        model.addAttribute("successMessage", "Game added successfully.");

        return "create-product";
    }

    // Shows the Edit Game page.
    @GetMapping("/products/edit/{id}")
    public String showEditProduct(@PathVariable int id, Model model) {

        // Return to the products page if the game was not found
        Product product = productService.getGameById(id);
        if (product == null) {
            return "redirect:/products";
        }

        model.addAttribute("product", product);
        return "edit-product";
    }

    // Deletes a game by its game ID.
    @PostMapping("/products/delete/{id}")
    public String deleteProduct(
            @PathVariable("id") int id,
            RedirectAttributes redirectAttributes) {

        // Show a message on the products page based on the result
        if (productService.deleteProduct(id)) {
            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    "Game deleted successfully."
            );
        } else {
            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    "The game could not be deleted."
            );
        }

        return "redirect:/products";
    }

    // Processes updates submitted from the Edit Game form.
    @PostMapping("/products/edit")
    public String updateProduct(
            @Valid Product product,
            BindingResult bindingResult,
            Model model) {

        // Return to the form if validation errors are found
        if (bindingResult.hasErrors()) {
            return "edit-product";
        }

        // Show an error if the product could not be updated
        if (!productService.updateProduct(product)) {
            model.addAttribute(
                    "errorMessage",
                    "The game could not be updated. Please try again."
            );
            return "edit-product";
        }

        return "redirect:/products";
    }
}