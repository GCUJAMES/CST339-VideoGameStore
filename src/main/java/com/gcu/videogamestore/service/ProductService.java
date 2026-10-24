package com.gcu.videogamestore.service;

import org.springframework.stereotype.Service;

import com.gcu.videogamestore.data.GameDataService;
import com.gcu.videogamestore.model.Product;

@Service
public class ProductService {

    // Used to access the game database
    private final GameDataService gameDataService;

    public ProductService(GameDataService gameDataService) {
        this.gameDataService = gameDataService;
    }

    // Sends the product to the data service to be saved
    public boolean createProduct(Product product) {
        return gameDataService.createGame(product);
    }
}
