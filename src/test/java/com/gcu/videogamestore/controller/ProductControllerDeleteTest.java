package com.gcu.videogamestore.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.gcu.videogamestore.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

class ProductControllerDeleteTest {

    private final ProductService service = mock(ProductService.class);
    private final ProductController controller = new ProductController(service);

    @Test
    void deleteSuccessRedirectsWithSuccessMessage() {
        when(service.deleteProduct(1)).thenReturn(true);
        var attributes = new RedirectAttributesModelMap();

        assertEquals("redirect:/products", controller.deleteProduct(1, attributes));
        assertTrue(attributes.getFlashAttributes().containsKey("successMessage"));
    }

    @Test
    void deleteFailureRedirectsWithErrorMessage() {
        when(service.deleteProduct(2)).thenReturn(false);
        var attributes = new RedirectAttributesModelMap();

        assertEquals("redirect:/products", controller.deleteProduct(2, attributes));
        assertTrue(attributes.getFlashAttributes().containsKey("errorMessage"));
    }
}