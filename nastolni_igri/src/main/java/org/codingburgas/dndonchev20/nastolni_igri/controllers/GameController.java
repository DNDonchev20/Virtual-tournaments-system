package org.codingburgas.dndonchev20.nastolni_igri.controllers;

import org.codingburgas.dndonchev20.nastolni_igri.models.ChatMessage;
import org.codingburgas.dndonchev20.nastolni_igri.models.Game;
import org.codingburgas.dndonchev20.nastolni_igri.models.User;
import org.codingburgas.dndonchev20.nastolni_igri.models.UserPrincipal;
import org.codingburgas.dndonchev20.nastolni_igri.services.ChatMessageService;
import org.codingburgas.dndonchev20.nastolni_igri.services.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/games")
public class GameController {

    private final GameService gameService;
    private final ChatMessageService chatMessageService;

    @Autowired
    public GameController(GameService gameService, ChatMessageService chatMessageService) {
        this.gameService = gameService;
        this.chatMessageService = chatMessageService;
    }

    @GetMapping
    public String getGames(Model model) {
        List<Game> games = gameService.getAllGames();
        model.addAttribute("games", games);
        return "games/list";
    }

    @GetMapping("/{id}/chat")
    public String getChatMessages(@PathVariable Long id, Model model, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        Game game = gameService.getGameById(id);
        if (game == null) {
            model.addAttribute("errorMessage", "Game not found.");
            return "error-page";
        }

        List<ChatMessage> messages = chatMessageService.getMessagesByGame(id);
        User user = convertUserPrincipalToUser(userPrincipal);

        model.addAttribute("game", game);
        model.addAttribute("messages", messages);
        model.addAttribute("message", new ChatMessage());
        model.addAttribute("user", user);

        return "games/chat";
    }


    public User convertUserPrincipalToUser(UserPrincipal userPrincipal) {
        User user = new User();
        user.setId(userPrincipal.getId());
        user.setUsername(userPrincipal.getUsername());
        return user;
    }

    @PostMapping("/{id}/chat")
    public String postChatMessage(@PathVariable Long id,
                                  @RequestParam String messageContent,
                                  @AuthenticationPrincipal UserPrincipal userPrincipal) {
        Game game = gameService.getGameById(id);
        if (game == null) {
            return "redirect:/games";
        }

        ChatMessage message = new ChatMessage();
        message.setGame(game);
        message.setMessage(messageContent);

        User user = convertUserPrincipalToUser(userPrincipal);
        message.setUser(user);

        chatMessageService.sendMessage(message);

        return "redirect:/games/" + id + "/chat";
    }

    @GetMapping("/create")
    public String showCreateGamePage() {
        return "games/create";
    }

    @PostMapping("/create")
    public String createGame(@RequestParam String name, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        if (name == null || name.isEmpty()) {
            return "redirect:/games/create?error=Game name cannot be empty";
        }

        // Convert UserPrincipal to User
        User user = convertUserPrincipalToUser(userPrincipal);

        Game game = new Game();
        game.setName(name);
        game.setCreator(user);
        gameService.saveGame(game);

        return "redirect:/games";
    }
}
