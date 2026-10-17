package com.gcu.videogamestore.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import jakarta.validation.constraints.NotEmpty;

/**
 * Represents a video game product in the GameForge application.
 * This model stores product information and validation rules.
 */
public class Product {

    // Unique identifier for the product
    private int productId;

    // Name of the video game
    @NotBlank(message = "Title is required.")
    @Size(max = 100, message = "Title must be 100 characters or less.")
    private String title;

    // Price of the product
    @NotNull(message = "Price is required.")
    @DecimalMin(value = "0.0", inclusive = true, message = "Price cannot be negative.")
    private BigDecimal price;

    // Platforms that the game is available on
    @NotEmpty(message = "Select at least one platform.")
    private List<String> platforms = new ArrayList<>();

    // Release date of the game
    @NotNull(message = "Release date is required.")
    private LocalDate releaseDate;

    // Description of the game
    @NotBlank(message = "Description is required.")
    @Size(max = 500, message = "Description must be 500 characters or less.")
    private String description;

    // Category or genre of the game
    @NotBlank(message = "Category is required.")
    private String category;

    /**
     * Default constructor used by Spring and Thymeleaf.
     */
    public Product() {
    }

    // Getters and setters

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public List<String> getPlatforms() {
        return platforms;
    }

    public void setPlatforms(List<String> platforms) {
        this.platforms = platforms;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
