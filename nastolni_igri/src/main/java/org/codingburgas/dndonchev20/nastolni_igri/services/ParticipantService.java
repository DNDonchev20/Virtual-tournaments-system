package org.codingburgas.dndonchev20.nastolni_igri.services;

import org.codingburgas.dndonchev20.nastolni_igri.models.Game;
import org.codingburgas.dndonchev20.nastolni_igri.models.Participant;
import org.codingburgas.dndonchev20.nastolni_igri.models.User;
import org.codingburgas.dndonchev20.nastolni_igri.repositories.ParticipantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ParticipantService {
    private final ParticipantRepository participantRepository;

    @Autowired
    public ParticipantService(ParticipantRepository participantRepository) {
        this.participantRepository = participantRepository;
    }

    public List<Participant> getParticipantsByGame(Long gameId) {
        return participantRepository.findByGameId(gameId);
    }

    public void addParticipant(Game game, User user) {
        if (participantRepository.existsByGameAndUser(game, user)) {
            return;
        }

        Participant participant = new Participant();
        participant.setGame(game);
        participant.setUser(user);
        participantRepository.save(participant);
    }

    public void updateParticipant(Participant participant) {
        participantRepository.save(participant);
    }


    public List<Participant> getRankedParticipants(Long gameId) {
        return participantRepository.findByGameIdOrderByScoreDesc(gameId);
    }
}
