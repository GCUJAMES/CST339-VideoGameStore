package com.gcu.videogamestore.controller;

import com.gcu.videogamestore.SecurityConfig;
import com.gcu.videogamestore.service.DatabaseUserDetailsService;
import com.gcu.videogamestore.service.ProductService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

@WebMvcTest(ProductController.class)
@Import(SecurityConfig.class)
class ProductSecurityTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductService productService;

    @MockitoBean
    private DatabaseUserDetailsService userDetailsService;

    @Test
    void anonymousUsersAreRedirectedFromProductPages() throws Exception {
        mockMvc.perform(get("/products"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));

        mockMvc.perform(get("/products/create"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));

        mockMvc.perform(get("/products/edit/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));

        verifyNoInteractions(productService);
    }

    @Test
    void anonymousUsersCannotModifyProducts() throws Exception {
        mockMvc.perform(post("/products/create").with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));

        mockMvc.perform(post("/products/edit").with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));

        mockMvc.perform(post("/products/delete/1").with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));

        verifyNoInteractions(productService);
    }

    @Test
    void authenticatedUsersCanViewProducts() throws Exception {
        mockMvc.perform(get("/products").with(user("gameforge")))
                .andExpect(status().isOk())
                .andExpect(view().name("products"))
                .andExpect(model().attribute("username", "gameforge"));

        verify(productService).getAllGames();
    }

    @Test
    void productChangesRequireCsrfToken() throws Exception {
        mockMvc.perform(post("/products/delete/1").with(user("gameforge")))
                .andExpect(status().isForbidden());

        verifyNoInteractions(productService);
    }
}