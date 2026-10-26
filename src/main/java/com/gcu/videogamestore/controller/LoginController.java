package com.gcu.videogamestore.controller;

import com.gcu.videogamestore.model.Login;
import com.gcu.videogamestore.service.LoginService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * Provides database-backed login and logout behavior.
 */
@Controller
public class LoginController {

    private final LoginService loginService;

    /**
     * Creates the controller with its login business service.
     *
    * @param loginService the injected service that evaluates stored credentials
     */
    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

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
    * Validates credentials and records a successful login in session.
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

        if (!loginService.authenticate(login)) {
            bindingResult.reject("invalidCredentials", "Invalid username or password. Please try again.");
            return "login";
        }

        session.setAttribute("loggedIn", true);
        session.setAttribute("username", loginService.getAuthenticatedUsername(login));
        return "redirect:/products";
    }

    /**
    * Clears the authenticated session and returns to the home page.
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