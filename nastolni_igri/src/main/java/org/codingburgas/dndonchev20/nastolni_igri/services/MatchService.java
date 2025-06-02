package org.codingburgas.dndonchev20.nastolni_igri.services;

import org.codingburgas.dndonchev20.nastolni_igri.models.Game;
import org.codingburgas.dndonchev20.nastolni_igri.models.Match;
import org.codingburgas.dndonchev20.nastolni_igri.repositories.MatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class MatchService {

    private final MatchRepository matchRepository;
    private final GameService gameService;

    @Autowired
    public MatchService(MatchRepository matchRepository, GameService gameService) {
        this.matchRepository = matchRepository;
        this.gameService = gameService;
    }

    public List<Match> getMatchesByGame(Long gameId) {
        Game game = gameService.getGameById(gameId);
        return matchRepository.findByTournamentOrderByIdDesc(game); // ✅ Correct
    }

    public void saveMatch(Match match) {
        matchRepository.save(match);
    }

    public List<Match> getTournamentMatches(Long gameId) {
        Game game = gameService.getGameById(gameId);
        return matchRepository.findByTournamentAndStatusOrderByIdDesc(game, "COMPLETED");
    }

}
