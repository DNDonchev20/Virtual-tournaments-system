package org.codingburgas.dndonchev20.nastolni_igri;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.codingburgas.dndonchev20.nastolni_igri.models.Game;
import org.codingburgas.dndonchev20.nastolni_igri.repositories.GameRepository;
import org.codingburgas.dndonchev20.nastolni_igri.services.GameService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GameServiceTest {

    @Mock
    private GameRepository gameRepository;

    @InjectMocks
    private GameService gameService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllGamesReturnsList() {
        Game game1 = new Game();
        game1.setName("Game One");
        Game game2 = new Game();
        game2.setName("Game Two");

        when(gameRepository.findAll()).thenReturn(Arrays.asList(game1, game2));

        List<Game> games = gameService.getAllGames();
        assertNotNull(games, "Games list should not be null");
        assertEquals(2, games.size(), "Should return 2 games");
        verify(gameRepository, times(1)).findAll();
    }

    @Test
    public void testGetAllGamesReturnsEmptyList() {
        when(gameRepository.findAll()).thenReturn(Collections.emptyList());
        List<Game> games = gameService.getAllGames();
        assertNotNull(games, "Games list should not be null");
        assertTrue(games.isEmpty(), "Games list should be empty");
        verify(gameRepository, times(1)).findAll();
    }
}
