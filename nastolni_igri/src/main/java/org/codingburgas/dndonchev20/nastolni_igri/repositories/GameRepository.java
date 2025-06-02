package org.codingburgas.dndonchev20.nastolni_igri.repositories;

import org.codingburgas.dndonchev20.nastolni_igri.models.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {

    List<Game> findByCreator_Id(Long creatorId);

    // Method for finding all games that a user has participated in
    List<Game> findByParticipants_User_Id(Long userId);
}
