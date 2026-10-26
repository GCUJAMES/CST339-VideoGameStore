package com.gcu.videogamestore.controller;

import com.gcu.videogamestore.data.entity.UserEntity;
import com.gcu.videogamestore.data.repository.UserRepository;
import com.gcu.videogamestore.model.Login;
import com.gcu.videogamestore.service.LoginService;
import com.gcu.videogamestore.service.ProductService;

import jakarta.servlet.http.HttpSession;

import org.junit.jupiter.api.Test;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.validation.BeanPropertyBindingResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Tests the login controller with database-backed users.
 */
class LoginControllerTests {

    private final UserRepository userRepository =
            mock(UserRepository.class);

    private final LoginController loginController =
            new LoginController(new LoginService(userRepository));

    private final ProductController productController =
            new ProductController(mock(ProductService.class));

    @Test
    void validCredentialsCreateLoggedInSessionAndRedirectToProducts() {

        Login credentials =
                login("gameforge", "GameForge123");

        BeanPropertyBindingResult bindingResult =
                new BeanPropertyBindingResult(credentials, "login");

        HttpSession session =
                mock(HttpSession.class);

        UserEntity user = new UserEntity(
                "Test",
                "User",
                "test@email.com",
                "5551234567",
                "gameforge",
                "GameForge123"
        );

        // Returns the stored user from the database
        when(userRepository.findByUsername("gameforge"))
                .thenReturn(java.util.Optional.of(user));

        String view =
                loginController.processLogin(
                        credentials,
                        bindingResult,
                        session);

        assertEquals("redirect:/products", view);

        verify(session)
                .setAttribute("loggedIn", true);

        verify(session)
                .setAttribute("username", "gameforge");
    }

    @Test
    void invalidCredentialsReturnToLoginWithHelpfulError() {

        Login credentials =
                login("gameforge", "incorrect");

        BeanPropertyBindingResult bindingResult =
                new BeanPropertyBindingResult(credentials, "login");

        UserEntity user = new UserEntity(
                "Test",
                "User",
                "test@email.com",
                "5551234567",
                "gameforge",
                "GameForge123"
        );

        when(userRepository.findByUsername("gameforge"))
                .thenReturn(java.util.Optional.of(user));

        String view =
                loginController.processLogin(
                        credentials,
                        bindingResult,
                        mock(HttpSession.class));

        assertEquals("login", view);

        assertTrue(bindingResult.hasGlobalErrors());

        assertNotNull(bindingResult.getGlobalError());

        assertEquals(
                "Invalid username or password. Please try again.",
                bindingResult.getGlobalError().getDefaultMessage());
    }

    @Test
    void productsRequireSuccessfulLogin() {

        String view =
                productController.showProducts(
                        mock(HttpSession.class),
                        new ExtendedModelMap());

        assertEquals("redirect:/login", view);
    }

    @Test
    void logoutInvalidatesTheSession() {

        HttpSession session =
                mock(HttpSession.class);

        String view =
                loginController.logout(session);

        assertEquals("redirect:/", view);

        verify(session).invalidate();
    }

    private Login login(String username, String password) {

        Login login = new Login();

        login.setUsername(username);
        login.setPassword(password);

        return login;
    }
}
