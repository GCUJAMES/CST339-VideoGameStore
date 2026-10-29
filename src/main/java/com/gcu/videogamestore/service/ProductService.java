package com.gcu.videogamestore.service;

import org.springframework.stereotype.Service;

import com.gcu.videogamestore.data.GameDataService;
import com.gcu.videogamestore.model.Product;

import java.util.List;

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
    
 	// Gets all games from the database
    public List<Product> getAllGames() {
        return gameDataService.getAllGames();
    }
}
