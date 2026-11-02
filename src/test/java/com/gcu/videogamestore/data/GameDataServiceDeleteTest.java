package com.gcu.videogamestore.data;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;

class GameDataServiceDeleteTest {
    private final JdbcTemplate jdbc = mock(JdbcTemplate.class);
    private final GameDataService service = new GameDataService(jdbc);

    @Test
    void deleteGameReturnsTrueWhenRowDeleted() {
        when(jdbc.update(anyString(), eq(5))).thenReturn(1);
        assertTrue(service.deleteGame(5));
    }

    @Test
    void deleteGameReturnsFalseWhenNoRow() {
        when(jdbc.update(anyString(), eq(99))).thenReturn(0);
        assertFalse(service.deleteGame(99));
    }
}