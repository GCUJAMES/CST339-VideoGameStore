package com.gcu.videogamestore.controller;

import com.gcu.videogamestore.model.Product;
import com.gcu.videogamestore.service.ProductService;
import java.util.List;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Exposes the current GameForge catalog in formats protected by Spring Security.
 */
@RestController
@RequestMapping("/service")
public class GameRestController {

    private final ProductService productService;

    public GameRestController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping(value = "/getjson", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Product> getGamesAsJson() {
        return productService.getAllGames();
    }

    @GetMapping(value = "/getxml", produces = MediaType.APPLICATION_XML_VALUE)
    public List<Product> getGamesAsXml() {
        return productService.getAllGames();
    }
}