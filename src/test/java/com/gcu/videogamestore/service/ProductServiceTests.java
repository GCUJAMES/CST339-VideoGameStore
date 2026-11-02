package com.gcu.videogamestore.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.gcu.videogamestore.data.GameDataService;
import com.gcu.videogamestore.model.Product;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProductServiceTests {

    @Mock
    private GameDataService gameDataService;

    @InjectMocks
    private ProductService productService;

    @Test
    void getGameByIdDelegatesToDataService() {
        Product product = new Product();
        product.setProductId(7);
        when(gameDataService.getGameById(7)).thenReturn(product);

        assertThat(productService.getGameById(7)).isSameAs(product);
    }

    @Test
    void updateProductDelegatesToDataService() {
        Product product = new Product();
        product.setProductId(7);
        when(gameDataService.updateGame(product)).thenReturn(true);

        assertThat(productService.updateProduct(product)).isTrue();
        verify(gameDataService).updateGame(product);
    }
}