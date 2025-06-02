package org.codingburgas.dndonchev20.nastolni_igri.repositories;

import org.codingburgas.dndonchev20.nastolni_igri.models.Invitation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InvitationRepository extends JpaRepository<Invitation, Long> {
    List<Invitation> findByRecipientId(Long recipientId);

    List<Invitation> findByRecipientIdAndAcceptedFalse(Long userId);

    boolean existsByRecipientIdAndGameIdAndAcceptedTrue(Long userId, Long gameId);

    boolean existsBySenderIdAndGameId(Long userId, Long gameId);
}
