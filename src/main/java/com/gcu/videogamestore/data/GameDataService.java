package com.gcu.videogamestore.data;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import com.gcu.videogamestore.model.Product;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

@Repository
public class GameDataService {

    // Used to connect to the database
    private final JdbcTemplate jdbcTemplate;

    public GameDataService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Adds a game to the database
    public boolean createGame(Product product) {

        String sql = """
                INSERT INTO games
                (title, price, release_date, description, category)
                VALUES (?, ?, ?, ?, ?)
                """;

        KeyHolder keyHolder = new GeneratedKeyHolder();

        int rows = jdbcTemplate.update(connection -> {

            PreparedStatement statement =
                    connection.prepareStatement(
                            sql,
                            Statement.RETURN_GENERATED_KEYS);

            statement.setString(1, product.getTitle());
            statement.setBigDecimal(2, product.getPrice());
            statement.setDate(3, Date.valueOf(product.getReleaseDate()));
            statement.setString(4, product.getDescription());
            statement.setString(5, product.getCategory());

            return statement;

        }, keyHolder);  

        // Makes sure the game was added
        if (rows == 0 || keyHolder.getKey() == null) {
            return false;
        }

        // Gets the ID of the game that was just created
        int gameId = keyHolder.getKey().intValue();
        
        // Saves the selected platforms for the game
        savePlatforms(gameId, product);

        return true;
    }
    
    // Saves the platforms selected for the game
    private void savePlatforms(int gameId, Product product) {

        String platformSql = """
                SELECT platform_id
                FROM platforms
                WHERE platform_name = ?
                """;

        String gamePlatformSql = """
                INSERT INTO game_platforms
                (game_id, platform_id)
                VALUES (?, ?)
                """;

        // Goes through each platform the user selected
        for (String platform : product.getPlatforms()) {

            // Finds the platform ID from the database
            Integer platformId = jdbcTemplate.queryForObject(
                    platformSql,
                    Integer.class,
                    platform
            );

            // Connects the game to that platform
            jdbcTemplate.update(
                    gamePlatformSql,
                    gameId,
                    platformId
            );
        }
    }
}
