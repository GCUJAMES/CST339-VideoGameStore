package com.gcu.videogamestore.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Displays the logged-in product landing page for the simulated Milestone 2 flow.
 */
@Controller
public class ProductController {

    /**
     * Shows the product page only after the simulated login flow has completed.
     *
     * @param session the current browser session
     * @param model the model used by the Thymeleaf view
     * @return the products view or a redirect to login
     */
    @GetMapping("/products")
    public String showProducts(HttpSession session, Model model) {
        if (!Boolean.TRUE.equals(session.getAttribute("loggedIn"))) {
            return "redirect:/login";
        }

        model.addAttribute("username", session.getAttribute("username"));
        return "products";
    }
}