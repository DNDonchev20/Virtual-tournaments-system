package org.codingburgas.dndonchev20.nastolni_igri.controllers;

import org.codingburgas.dndonchev20.nastolni_igri.models.*;
import org.codingburgas.dndonchev20.nastolni_igri.services.ParticipantService;
import org.codingburgas.dndonchev20.nastolni_igri.services.GameService;
import org.codingburgas.dndonchev20.nastolni_igri.services.MatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/participants")
public class ParticipantController {

    private final ParticipantService participantService;
    private final GameService gameService;
    private final MatchService matchService;

    @Autowired
    public ParticipantController(ParticipantService participantService, GameService gameService, MatchService matchService) {
        this.participantService = participantService;
        this.gameService = gameService;
        this.matchService = matchService;
    }

    // Show participants for a specific game
    @GetMapping("/list/{gameId}")
    public String showParticipants(@PathVariable Long gameId, Model model) {
        Game game = gameService.getGameById(gameId);
        if (game == null) {
            model.addAttribute("errorMessage", "Game not found.");
            return "error-page";
        }

        List<Participant> participants = participantService.getParticipantsByGame(gameId);
        model.addAttribute("participants", participants);
        model.addAttribute("gameId", gameId);
        return "participants/list";
    }

    @PostMapping("/{gameId}/join")
    public String joinGame(@PathVariable Long gameId, @AuthenticationPrincipal UserPrincipal userPrincipal, Model model) {
        if (userPrincipal == null) {
            model.addAttribute("errorMessage", "User is not authenticated.");
            return "error-page";
        }

        Game game = gameService.getGameById(gameId);
        if (game == null) {
            model.addAttribute("errorMessage", "Game not found.");
            return "error-page";
        }

        User user = new User();
        user.setId(userPrincipal.getId());
        user.setUsername(userPrincipal.getUsername());

        participantService.addParticipant(game, user);

        return "redirect:/participants/list/" + gameId;
    }

    @PostMapping("/{gameId}/create-match")
    public String createMatch(@PathVariable Long gameId, Model model) {
        List<Participant> participants = participantService.getParticipantsByGame(gameId);

        if (participants.size() < 2) {
            model.addAttribute("errorMessage", "Not enough participants to create a match!");
            return "participants/list";
        }

        Participant player1 = participants.get(0);
        Participant player2 = participants.get(1);

        // Determine result and assign points consistently.
        // For example, decide randomly which player wins:
        boolean player1Wins = Math.random() < 0.5;
        int player1Points, player2Points;

        if (player1Wins) {
            player1Points = 3;
            player2Points = 1;
        } else {
            player1Points = 1;
            player2Points = 3;
        }
        // (Optionally, you can introduce a tie chance if desired)

        // Create and save the match with the fixed point values.
        Game game = gameService.getGameById(gameId);
        Match match = new Match(game, player1.getUser(), player2.getUser(), player1Points, player2Points, "COMPLETED");
        matchService.saveMatch(match);

        // Update the participants' scores with the same point values.
        player1.setScore(player1.getScore() + player1Points);
        player2.setScore(player2.getScore() + player2Points);
        participantService.updateParticipant(player1);
        participantService.updateParticipant(player2);

        return "redirect:/participants/ranking/" + gameId;
    }

    @GetMapping("/ranking/{gameId}")
    public String ranking(@PathVariable Long gameId, Model model) {
        List<Participant> rankedPlayers = participantService.getRankedParticipants(gameId);
        model.addAttribute("rankedPlayers", rankedPlayers);
        return "participants/ranking";
    }

    @GetMapping("/history/{gameId}")
    public String matchHistory(@PathVariable Long gameId, Model model) {
        List<Match> matchHistory = matchService.getMatchesByGame(gameId);
        model.addAttribute("matchHistory", matchHistory);
        return "participants/matchHistory";
    }
}
