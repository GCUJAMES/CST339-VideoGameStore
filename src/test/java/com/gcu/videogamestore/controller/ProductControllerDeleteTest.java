package com.gcu.videogamestore.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;
import com.gcu.videogamestore.service.ProductService;
import static org.mockito.ArgumentMatchers.anyInt;

class ProductControllerDeleteTest {
    private final ProductService service = mock(ProductService.class);
    private final ProductController controller = new ProductController(service);

    private MockHttpSession loggedIn() {
        MockHttpSession s = new MockHttpSession();
        s.setAttribute("loggedIn", true);
        return s;
    }

    @Test
    void deleteSuccessRedirectsWithSuccessMessage() {
        when(service.deleteProduct(1)).thenReturn(true);
        var ra = new RedirectAttributesModelMap();
        assertEquals("redirect:/products", controller.deleteProduct(1, loggedIn(), ra));
        assertTrue(ra.getFlashAttributes().containsKey("successMessage"));
    }

    @Test
    void deleteFailureRedirectsWithErrorMessage() {
        when(service.deleteProduct(2)).thenReturn(false);
        var ra = new RedirectAttributesModelMap();
        assertEquals("redirect:/products", controller.deleteProduct(2, loggedIn(), ra));
        assertTrue(ra.getFlashAttributes().containsKey("errorMessage"));
    }

    @Test
    void deleteRequiresLogin() {
        var ra = new RedirectAttributesModelMap();
        assertEquals("redirect:/login", controller.deleteProduct(1, new MockHttpSession(), ra));
        verify(service, never()).deleteProduct(anyInt());
    }
}