package com.gcu.videogamestore.data;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.gcu.videogamestore.model.Product;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

class GameDataServiceGetAllTest {

    @Test
    void loadsGamesAndPlatformsInTwoQueries() {
        Product firstGame = product(1, "First Game");
        Product secondGame = product(2, "Second Game");
        List<Product> games = new ArrayList<>(List.of(firstGame, secondGame));
        List<Map.Entry<Integer, String>> platformRows = List.of(
                Map.entry(1, "PC"),
                Map.entry(1, "Xbox"),
                Map.entry(2, "PlayStation")
        );

        StubJdbcTemplate jdbcTemplate = new StubJdbcTemplate(games, platformRows);
        GameDataService service = new GameDataService(jdbcTemplate);

        List<Product> result = service.getAllGames();

        assertEquals(List.of("PC", "Xbox"), result.get(0).getPlatforms());
        assertEquals(List.of("PlayStation"), result.get(1).getPlatforms());
        assertEquals(2, jdbcTemplate.getQueryCount());
    }

    private Product product(int id, String title) {
        Product product = new Product();
        product.setProductId(id);
        product.setTitle(title);
        return product;
    }

        private static class StubJdbcTemplate extends JdbcTemplate {

                private final List<Product> games;
                private final List<Map.Entry<Integer, String>> platformRows;
                private int queryCount;

                StubJdbcTemplate(
                                List<Product> games,
                                List<Map.Entry<Integer, String>> platformRows) {
                        this.games = games;
                        this.platformRows = platformRows;
                }

                @Override
                @SuppressWarnings("unchecked")
                public <T> List<T> query(String sql, RowMapper<T> rowMapper) {
                        queryCount++;
                        return (List<T>) (sql.contains("FROM game_platforms") ? platformRows : games);
                }

                int getQueryCount() {
                        return queryCount;
                }
        }
}
