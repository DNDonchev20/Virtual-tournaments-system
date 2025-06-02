package org.codingburgas.dndonchev20.nastolni_igri.services;

import org.codingburgas.dndonchev20.nastolni_igri.models.Game;
import org.codingburgas.dndonchev20.nastolni_igri.repositories.GameRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GameService {
    private final GameRepository gameRepository;

    public GameService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public List<Game> getAllGames() {
        return gameRepository.findAll();
    }

    public Game getGameById(Long id) {
        return gameRepository.findById(id).orElse(null);
    }

    public void saveGame(Game newGame) {
        gameRepository.save(newGame);
    }
}
