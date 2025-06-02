package org.codingburgas.dndonchev20.nastolni_igri.controllers;

import org.codingburgas.dndonchev20.nastolni_igri.models.ChatMessage;
import org.codingburgas.dndonchev20.nastolni_igri.models.Game;
import org.codingburgas.dndonchev20.nastolni_igri.models.Participant;
import org.codingburgas.dndonchev20.nastolni_igri.models.User;
import org.codingburgas.dndonchev20.nastolni_igri.services.ChatMessageService;
import org.codingburgas.dndonchev20.nastolni_igri.services.GameService;
import org.codingburgas.dndonchev20.nastolni_igri.services.ParticipantService;
import org.codingburgas.dndonchev20.nastolni_igri.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/chat")
public class ChatMessageController {

    private final ChatMessageService chatMessageService;
    private final ParticipantService participantService;
    private final GameService gameService;
    private final UserService userService;

    @Autowired
    public ChatMessageController(ChatMessageService chatMessageService,
                                 ParticipantService participantService,
                                 GameService gameService,
                                 UserService userService) {
        this.chatMessageService = chatMessageService;
        this.participantService = participantService;
        this.gameService = gameService;
        this.userService = userService;
    }

    @GetMapping("/{gameId}")
    public String getParticipantsAndMessages(@PathVariable Long gameId, Model model) {
        Game game = gameService.getGameById(gameId);
        if (game == null) {
            model.addAttribute("errorMessage", "Game not found.");
            return "error-page";
        }

        List<Participant> participants = participantService.getParticipantsByGame(gameId);
        List<ChatMessage> messages = chatMessageService.getMessagesByGame(gameId);

        model.addAttribute("game", game);
        model.addAttribute("participants", participants);
        model.addAttribute("messages", messages);

        return "participants/list"; // Thymeleaf template for participants and messages
    }

    @PostMapping("/send-message")
    public String sendMessage(@RequestParam Long gameId,
                              @RequestParam Long userId,
                              @RequestParam String content,
                              Model model) {
        Game game = gameService.getGameById(gameId);
        if (game == null) {
            model.addAttribute("errorMessage", "Invalid game.");
            return "error-page";
        }

        User user = userService.getUserById(userId);
        if (user == null) {
            model.addAttribute("errorMessage", "Invalid user.");
            return "error-page";
        }

        ChatMessage message = new ChatMessage();
        message.setGame(game);
        message.setUser(user);
        message.setMessage(content);

        chatMessageService.sendMessage(message);

        return "redirect:/chat/" + gameId;
    }

    @GetMapping("/messages/{gameId}")
    @ResponseBody
    public List<ChatMessage> showChatMessages(@PathVariable Long gameId) {
        return chatMessageService.getMessagesByGame(gameId);
    }
}
