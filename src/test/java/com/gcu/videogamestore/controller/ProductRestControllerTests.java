package com.gcu.videogamestore.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.gcu.videogamestore.SecurityConfig;
import com.gcu.videogamestore.model.Product;
import com.gcu.videogamestore.service.DatabaseUserDetailsService;
import com.gcu.videogamestore.service.ProductService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.User;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductRestController.class)
@Import(SecurityConfig.class)
class ProductRestControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductService productService;

    @MockitoBean
    private DatabaseUserDetailsService userDetailsService;

    @BeforeEach
    void setUp() {
        when(userDetailsService.loadUserByUsername("gameforge"))
                .thenReturn(User.withUsername("gameforge")
                        .password("password")
                        .roles("USER")
                        .build());
    }

    @Test
    void authenticatedUserCanGetProductsAsJson() throws Exception {
        when(productService.getAllGames()).thenReturn(List.of(sampleProduct()));

        mockMvc.perform(get("/service/products")
                    .with(httpBasic("gameforge", "password"))
                    .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].productId").value(7))
                .andExpect(jsonPath("$[0].title").value("Example Game"));

        verify(productService).getAllGames();
    }

    @Test
    void authenticatedUserCanGetOneProductAsJson() throws Exception {
        when(productService.getGameById(7)).thenReturn(sampleProduct());

        mockMvc.perform(get("/service/products/7")
                    .with(httpBasic("gameforge", "password"))
                    .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.productId").value(7))
                .andExpect(jsonPath("$.title").value("Example Game"))
                .andExpect(jsonPath("$.price").value(19.99))
                .andExpect(jsonPath("$.releaseDate").value("2025-01-15"))
                .andExpect(jsonPath("$.platforms[0]").value("PC"));

        verify(productService).getGameById(7);
    }

    @Test
    void missingProductReturnsNotFound() throws Exception {
        when(productService.getGameById(999)).thenReturn(null);

        mockMvc.perform(get("/service/products/999")
                    .with(httpBasic("gameforge", "password"))
                    .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string(""));

        verify(productService).getGameById(999);
    }

    @Test
    void nonNumericProductIdReturnsBadRequest() throws Exception {
        mockMvc.perform(get("/service/products/not-a-number")
                    .with(httpBasic("gameforge", "password"))
                    .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(productService);
    }

    @Test
    void unauthenticatedApiRequestReturnsUnauthorized() throws Exception {
        mockMvc.perform(get("/service/products/7")
                    .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());

        verifyNoInteractions(productService);
    }

    private Product sampleProduct() {
        Product product = new Product();
        product.setProductId(7);
        product.setTitle("Example Game");
        product.setPrice(new BigDecimal("19.99"));
        product.setReleaseDate(LocalDate.of(2025, 1, 15));
        product.setDescription("An example game.");
        product.setCategory("Adventure");
        product.setPlatforms(List.of("PC"));
        return product;
    }
}