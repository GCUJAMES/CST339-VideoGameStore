package com.gcu.videogamestore.controller;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.gcu.videogamestore.SecurityConfig;
import com.gcu.videogamestore.model.Product;
import com.gcu.videogamestore.service.DatabaseUserDetailsService;
import com.gcu.videogamestore.service.ProductService;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;

@WebMvcTest(GameRestController.class)
@Import({SecurityConfig.class, GameRestControllerSecurityTests.TestSecurityConfiguration.class})
class GameRestControllerSecurityTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductService productService;

    @MockitoBean
    private DatabaseUserDetailsService databaseUserDetailsService;

    @Test
    void blocksUnauthenticatedJsonRequests() throws Exception {
        mockMvc.perform(get("/service/getjson"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void allowsDatabaseUserToRetrieveJson() throws Exception {
        configureAuthenticatedUser();
        when(productService.getAllGames()).thenReturn(List.of(sampleGame()));

        mockMvc.perform(get("/service/getjson").with(httpBasic("gameforge", "password")))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/json"));
    }

    @Test
    void allowsDatabaseUserToRetrieveXml() throws Exception {
        configureAuthenticatedUser();
        when(productService.getAllGames()).thenReturn(List.of(sampleGame()));

        mockMvc.perform(get("/service/getxml").with(httpBasic("gameforge", "password")))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/xml"));
    }

    @Test
    void blocksUnauthenticatedXmlRequests() throws Exception {
        mockMvc.perform(get("/service/getxml"))
                .andExpect(status().isUnauthorized());
    }

    private void configureAuthenticatedUser() {
        when(databaseUserDetailsService.loadUserByUsername("gameforge"))
                .thenReturn(User.withUsername("gameforge")
                        .password("password")
                        .roles("USER")
                        .build());
    }

    private Product sampleGame() {
        Product product = new Product();
        product.setProductId(1);
        product.setTitle("GameForge Demo");
        product.setPrice(new BigDecimal("19.99"));
        product.setPlatforms(List.of("PC"));
        product.setReleaseDate(LocalDate.of(2026, 1, 1));
        product.setDescription("A protected API test game.");
        product.setCategory("Action");
        return product;
    }

    @TestConfiguration
    @EnableWebSecurity
    static class TestSecurityConfiguration {
    }
}