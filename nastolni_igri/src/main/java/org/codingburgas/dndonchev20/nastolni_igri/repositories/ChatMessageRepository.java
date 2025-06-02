package org.codingburgas.dndonchev20.nastolni_igri.repositories;

import org.codingburgas.dndonchev20.nastolni_igri.models.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findByGameIdOrderBySentAtAsc(Long gameId);
}
