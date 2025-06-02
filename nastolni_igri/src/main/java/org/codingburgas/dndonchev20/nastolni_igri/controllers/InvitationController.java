package org.codingburgas.dndonchev20.nastolni_igri.controllers;

import org.codingburgas.dndonchev20.nastolni_igri.models.Game;
import org.codingburgas.dndonchev20.nastolni_igri.models.Invitation;
import org.codingburgas.dndonchev20.nastolni_igri.models.User;
import org.codingburgas.dndonchev20.nastolni_igri.models.UserPrincipal;
import org.codingburgas.dndonchev20.nastolni_igri.services.GameService;
import org.codingburgas.dndonchev20.nastolni_igri.services.InvitationService;
import org.codingburgas.dndonchev20.nastolni_igri.services.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/invitations")
public class InvitationController {
    private final InvitationService invitationService;
    private final UserService userService;
    private final GameService gameService;

    public InvitationController(InvitationService invitationService, UserService userService, GameService gameService) {
        this.invitationService = invitationService;
        this.userService = userService;
        this.gameService = gameService;
    }

    @GetMapping
    public String listInvitations(@AuthenticationPrincipal UserPrincipal userPrincipal, Model model) {
        if (userPrincipal == null) {
            throw new IllegalStateException("User is not authenticated");
        }

        List<Invitation> invitations = invitationService.getPendingInvitationsForUser(userPrincipal.getId());
        model.addAttribute("invitations", invitations);
        return "invitations/list";
    }

    @GetMapping("/send")
    public String showSendInvitationForm(@RequestParam Long gameId, Model model) {
        if (gameId == null) {
            throw new IllegalArgumentException("Game ID is required.");
        }

        Game game = gameService.getGameById(gameId);
        if (game == null) {
            throw new IllegalArgumentException("Game not found.");
        }

        model.addAttribute("game", game);
        model.addAttribute("users", userService.getAllUsers());
        return "invitations/send";
    }

    @PostMapping("/send")
    public String sendInvitation(@RequestParam Long gameId, @RequestParam Long recipientId, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        if (userPrincipal == null) {
            throw new IllegalStateException("User is not authenticated.");
        }

        Game game = gameService.getGameById(gameId);
        if (game == null) {
            throw new IllegalArgumentException("Game not found.");
        }

        Invitation invitation = new Invitation();
        invitation.setGame(game);

        User sender = new User();
        sender.setId(userPrincipal.getId());
        invitation.setSender(sender);

        User recipient = new User();
        recipient.setId(recipientId);
        invitation.setRecipient(recipient);

        invitationService.sendInvitation(invitation);
        return "redirect:/games";
    }

    @PostMapping("/accept")
    public String acceptInvitation(@RequestParam Long invitationId, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        invitationService.acceptInvitation(invitationId, userPrincipal.getId());
        Invitation invitation = invitationService.findById(invitationId);

        if (invitation == null || invitation.getGame() == null) {
            throw new IllegalArgumentException("Invalid invitation");
        }

        return "redirect:/participants/list/" + invitation.getGame().getId();
    }

    @PostMapping("/decline")
    public String declineInvitation(@RequestParam Long invitationId, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        invitationService.declineInvitation(invitationId, userPrincipal.getId());
        return "redirect:/invitations";
    }
}
