package com.gcu.videogamestore.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.gcu.videogamestore.model.Product;
import com.gcu.videogamestore.service.ProductService;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.validation.BeanPropertyBindingResult;

class ProductControllerTests {

    private ProductService productService;
    private ProductController controller;
    private HttpSession loggedInSession;

    @BeforeEach
    void setUp() {
        productService = mock(ProductService.class);
        controller = new ProductController(productService);
        loggedInSession = mock(HttpSession.class);
        when(loggedInSession.getAttribute("loggedIn")).thenReturn(true);
    }

    @Test
    void showEditProductLoadsTheExistingGame() {
        Product product = new Product();
        product.setProductId(7);
        when(productService.getGameById(7)).thenReturn(product);
        ExtendedModelMap model = new ExtendedModelMap();

        assertEquals("edit-product", controller.showEditProduct(7, loggedInSession, model));
        assertSame(product, model.getAttribute("product"));
    }

    @Test
    void showEditProductRedirectsWhenGameDoesNotExist() {
        when(productService.getGameById(7)).thenReturn(null);

        assertEquals("redirect:/products", controller.showEditProduct(7, loggedInSession, new ExtendedModelMap()));
    }

    @Test
    void updateProductUsesServiceAndReturnsToGamesPage() {
        Product product = new Product();
        product.setProductId(7);
        when(productService.updateProduct(product)).thenReturn(true);

        assertEquals(
                "redirect:/products",
                controller.updateProduct(product, new BeanPropertyBindingResult(product, "product"), loggedInSession, new ExtendedModelMap())
        );
        verify(productService).updateProduct(product);
    }

    @Test
    void updateProductKeepsTheEditFormOpenForValidationErrors() {
        Product product = new Product();
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(product, "product");
        bindingResult.rejectValue("title", "NotBlank", "Title is required.");

        assertEquals("edit-product", controller.updateProduct(product, bindingResult, loggedInSession, new ExtendedModelMap()));
        verify(productService, never()).updateProduct(product);
    }
}