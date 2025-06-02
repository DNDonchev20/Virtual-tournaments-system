package org.codingburgas.dndonchev20.nastolni_igri.services;

import org.codingburgas.dndonchev20.nastolni_igri.models.Game;
import org.codingburgas.dndonchev20.nastolni_igri.models.Participant;
import org.codingburgas.dndonchev20.nastolni_igri.repositories.GameRepository;
import org.codingburgas.dndonchev20.nastolni_igri.repositories.ParticipantRepository;
import org.codingburgas.dndonchev20.nastolni_igri.repositories.UserRepository;
import org.codingburgas.dndonchev20.nastolni_igri.models.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TournamentService {
    private final GameRepository gameRepository;
    private final ParticipantRepository participantRepository;
    private final UserRepository userRepository;

    public TournamentService(GameRepository gameRepository, ParticipantRepository participantRepository, UserRepository userRepository) {
        this.gameRepository = gameRepository;
        this.participantRepository = participantRepository;
        this.userRepository = userRepository;
    }

    public void registerParticipant(Long gameId, Long userId) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new IllegalArgumentException("Tournament not found"));

        if (game.getCreator() == null) {
            // Set a default creator or use the currently authenticated user
            User defaultUser = userRepository.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));
            game.setCreator(defaultUser);
            gameRepository.save(game);
        }

        if (!game.isTournament()) {
            throw new IllegalStateException("This game is not marked as a tournament");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        boolean alreadyRegistered = participantRepository.existsByGameAndUser(game, user);
        if (!alreadyRegistered) {
            Participant participant = new Participant();
            participant.setGame(game);
            participant.setUser(user);
            participant.setScore(0);
            participantRepository.save(participant);
        }
    }

    public List<Participant> getParticipants(Long gameId) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new IllegalArgumentException("Tournament not found"));
        return participantRepository.findByGame(game);
    }
}