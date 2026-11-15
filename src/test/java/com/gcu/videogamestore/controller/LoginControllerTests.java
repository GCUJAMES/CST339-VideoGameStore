package com.gcu.videogamestore.controller;

import java.util.Optional;

import com.gcu.videogamestore.SecurityConfig;
import com.gcu.videogamestore.data.entity.UserEntity;
import com.gcu.videogamestore.data.repository.UserRepository;
import com.gcu.videogamestore.service.DatabaseUserDetailsService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LoginController.class)
@Import({SecurityConfig.class, DatabaseUserDetailsService.class})
class LoginControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        UserEntity user = new UserEntity(
                "Test",
                "User",
                "test@email.com",
                "5551234567",
                "gameforge",
                "GameForge123"
        );

        when(userRepository.findByUsername("gameforge"))
                .thenReturn(Optional.of(user));
    }

    @Test
    void validCredentialsAuthenticateAndRedirectToProducts() throws Exception {
        mockMvc.perform(post("/login")
                    .with(csrf())
                    .param("username", "gameforge")
                    .param("password", "GameForge123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/products"))
                .andExpect(authenticated().withUsername("gameforge"));
    }

    @Test
    void invalidCredentialsRedirectToLoginWithError() throws Exception {
        mockMvc.perform(post("/login")
                    .with(csrf())
                    .param("username", "gameforge")
                    .param("password", "incorrect"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?error"))
                .andExpect(unauthenticated());
    }

    @Test
    void logoutInvalidatesTheAuthenticatedSession() throws Exception {
        MvcResult loginResult = mockMvc.perform(post("/login")
                    .with(csrf())
                    .param("username", "gameforge")
                    .param("password", "GameForge123"))
                .andExpect(authenticated().withUsername("gameforge"))
                .andReturn();

        MockHttpSession session =
                (MockHttpSession) loginResult.getRequest().getSession(false);
        assertNotNull(session);

        mockMvc.perform(post("/logout")
                    .session(session)
                    .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                .andExpect(unauthenticated());

        assertTrue(session.isInvalid());
    }

    @Test
    void loginWithoutCsrfTokenIsRejected() throws Exception {
        mockMvc.perform(post("/login")
                    .param("username", "gameforge")
                    .param("password", "GameForge123"))
                .andExpect(status().isForbidden());
    }
}
