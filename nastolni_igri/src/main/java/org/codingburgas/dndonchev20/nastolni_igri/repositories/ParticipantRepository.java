package org.codingburgas.dndonchev20.nastolni_igri.repositories;

import org.codingburgas.dndonchev20.nastolni_igri.models.Game;
import org.codingburgas.dndonchev20.nastolni_igri.models.Participant;
import org.codingburgas.dndonchev20.nastolni_igri.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ParticipantRepository extends JpaRepository<Participant, Long> {
    List<Participant> findByGame(Game game);

    boolean existsByGameAndUser(Game game, User user);

    List<Participant> findByGameId(Long gameId);

    List<Participant> findByGameIdOrderByScoreDesc(Long gameId);
}
