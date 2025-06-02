package org.codingburgas.dndonchev20.nastolni_igri.controllers;

import org.codingburgas.dndonchev20.nastolni_igri.models.Game;
import org.codingburgas.dndonchev20.nastolni_igri.models.Match;
import org.codingburgas.dndonchev20.nastolni_igri.models.Participant;
import org.codingburgas.dndonchev20.nastolni_igri.models.User;
import org.codingburgas.dndonchev20.nastolni_igri.services.GameService;
import org.codingburgas.dndonchev20.nastolni_igri.services.MatchService;
import org.codingburgas.dndonchev20.nastolni_igri.services.ParticipantService;
import org.codingburgas.dndonchev20.nastolni_igri.services.TournamentService;
import org.codingburgas.dndonchev20.nastolni_igri.services.UserService;
import org.codingburgas.dndonchev20.nastolni_igri.utils.TournamentRankingUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/tournaments")
public class TournamentController {

    private final GameService gameService;
    private final TournamentService tournamentService;
    private final UserService userService;
    private final ParticipantService participantService;
    private final MatchService matchService;

    public TournamentController(GameService gameService,
                                TournamentService tournamentService,
                                UserService userService,
                                ParticipantService participantService,
                                MatchService matchService) {
        this.gameService = gameService;
        this.tournamentService = tournamentService;
        this.userService = userService;
        this.participantService = participantService;
        this.matchService = matchService;
    }

    // 1) Display a form to select an existing game (not yet a tournament)
    @GetMapping("/create")
    public String showCreateTournamentForm(Model model) {
        List<Game> games = gameService.getAllGames().stream()
                .filter(game -> !game.isTournament() && game.getCreator() != null)
                .collect(Collectors.toList());
        model.addAttribute("games", games);
        return "tournaments/selectGame";
    }

    // 2) Mark an existing game as a tournament and redirect to player selection
    @PostMapping("/selectGame")
    public String useExistingGameForTournament(@RequestParam Long gameId) {
        Game game = gameService.getGameById(gameId);
        if (game == null) {
            throw new IllegalArgumentException("Game not found");
        }
        game.setTournament(true);
        gameService.saveGame(game);
        return "redirect:/tournaments/selectPlayers/" + game.getId();
    }

    // 3) Show a form to select exactly two players for the tournament
    @GetMapping("/selectPlayers/{gameId}")
    public String showSelectPlayersForm(@PathVariable Long gameId, Model model) {
        List<User> allUsers = userService.getAllUsers();
        model.addAttribute("gameId", gameId);
        model.addAttribute("users", allUsers);
        return "tournaments/selectPlayers";
    }

    // 4) Register the two selected players into the tournament
    @PostMapping("/selectPlayers")
    public String selectPlayers(@RequestParam Long gameId,
                                @RequestParam Long player1Id,
                                @RequestParam Long player2Id) {
        tournamentService.registerParticipant(gameId, player1Id);
        tournamentService.registerParticipant(gameId, player2Id);
        return "redirect:/tournaments/bracket/" + gameId;
    }

    // 5) Display a simple bracket for two players
    @GetMapping("/bracket/{gameId}")
    public String showBracket(@PathVariable Long gameId, Model model) {
        model.addAttribute("gameId", gameId);
        model.addAttribute("participants", tournamentService.getParticipants(gameId));
        return "tournaments/bracket";
    }

    // 6) Create a tournament match using a simple result algorithm, update scores, and redirect to ranking
    @PostMapping("/createMatch")
    public String createTournamentMatch(@RequestParam Long gameId, Model model) {
        List<Participant> participants = participantService.getParticipantsByGame(gameId);
        if (participants.size() < 2) {
            model.addAttribute("errorMessage", "Not enough participants to create a match!");
            return "participants/list";
        }
        // For a two-player tournament, take the first two participants.
        Participant player1 = participants.get(0);
        Participant player2 = participants.get(1);

        // Use a simple algorithm to decide the match result.
        int[] points = TournamentRankingUtil.simpleTournamentResult();

        // Create and save the match record.
        Game game = gameService.getGameById(gameId);
        Match match = new Match(game, player1.getUser(), player2.getUser(), points[0], points[1], "COMPLETED");
        matchService.saveMatch(match);

        // Update the participants' scores.
        player1.setScore(player1.getScore() + points[0]);
        player2.setScore(player2.getScore() + points[1]);
        participantService.updateParticipant(player1);
        participantService.updateParticipant(player2);

        return "redirect:/participants/ranking/" + gameId;
    }
}
