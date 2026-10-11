package com.gcu.videogamestore.controller;

import com.gcu.videogamestore.model.Login;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * Provides simulated login and logout behavior for Milestone 2.
 * Database-backed authentication is intentionally deferred to Milestone 6.
 */
@Controller
public class LoginController {

    private static final String DEMO_USERNAME = "gameforge";
    private static final String DEMO_PASSWORD = "GameForge123";

    /**
     * Displays the login form.
     *
     * @param model the model used by the Thymeleaf view
     * @return the login view
     */
    @GetMapping("/login")
    public String showLoginForm(Model model) {
        if (!model.containsAttribute("login")) {
            model.addAttribute("login", new Login());
        }
        return "login";
    }

    /**
     * Validates credentials and records a successful simulated login in session.
     *
     * @param login submitted credentials
     * @param bindingResult validation results for the submitted form
     * @param session the current browser session
     * @return the products page after a successful login, otherwise the login view
     */
    @PostMapping("/login")
    public String processLogin(@Valid @ModelAttribute("login") Login login,
                               BindingResult bindingResult,
                               HttpSession session) {
        if (bindingResult.hasErrors()) {
            return "login";
        }

        if (!DEMO_USERNAME.equalsIgnoreCase(login.getUsername().trim())
                || !DEMO_PASSWORD.equals(login.getPassword())) {
            bindingResult.reject("invalidCredentials", "Invalid username or password. Please try again.");
            return "login";
        }

        session.setAttribute("loggedIn", true);
        session.setAttribute("username", DEMO_USERNAME);
        return "redirect:/products";
    }

    /**
     * Clears the simulated authenticated session and returns to the home page.
     *
     * @param session the current browser session
     * @return a redirect to the public home page
     */
    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}