package org.codingburgas.dndonchev20.nastolni_igri.services;

import org.codingburgas.dndonchev20.nastolni_igri.models.ChatMessage;
import org.codingburgas.dndonchev20.nastolni_igri.repositories.ChatMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatMessageService {
    private final ChatMessageRepository chatMessageRepository;

    @Autowired
    public ChatMessageService(ChatMessageRepository chatMessageRepository) {
        this.chatMessageRepository = chatMessageRepository;
    }

    public List<ChatMessage> getMessagesByGame(Long gameId) {
        return chatMessageRepository.findByGameIdOrderBySentAtAsc(gameId);
    }

    public ChatMessage sendMessage(ChatMessage message) {
        return chatMessageRepository.save(message);
    }
}
