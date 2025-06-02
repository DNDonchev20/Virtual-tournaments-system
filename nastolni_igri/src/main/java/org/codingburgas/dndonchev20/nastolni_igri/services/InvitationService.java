package org.codingburgas.dndonchev20.nastolni_igri.services;

import jakarta.transaction.Transactional;
import org.codingburgas.dndonchev20.nastolni_igri.models.Game;
import org.codingburgas.dndonchev20.nastolni_igri.models.Invitation;
import org.codingburgas.dndonchev20.nastolni_igri.models.Participant;
import org.codingburgas.dndonchev20.nastolni_igri.models.User;
import org.codingburgas.dndonchev20.nastolni_igri.repositories.InvitationRepository;
import org.codingburgas.dndonchev20.nastolni_igri.repositories.ParticipantRepository;
import org.codingburgas.dndonchev20.nastolni_igri.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InvitationService {
    private final InvitationRepository invitationRepository;
    private final ParticipantRepository participantRepository;
    private final UserRepository userRepository;

    public InvitationService(InvitationRepository invitationRepository, ParticipantRepository participantRepository, UserRepository userRepository) {
        this.participantRepository = participantRepository;
        this.userRepository = userRepository;
        this.invitationRepository = invitationRepository;
    }

    public void sendInvitation(Invitation invitation) {
        invitationRepository.save(invitation);
    }

    public List<Invitation> getPendingInvitationsForUser(Long userId) {
        return invitationRepository.findByRecipientIdAndAcceptedFalse(userId);
    }

    public void declineInvitation(Long invitationId, Long userId) {
        Invitation invitation = invitationRepository.findById(invitationId)
                .orElseThrow(() -> new IllegalArgumentException("Invitation not found"));

        if (!invitation.getRecipient().getId().equals(userId)) {
            throw new SecurityException("User is not authorized to decline this invitation");
        }

        invitationRepository.delete(invitation);
    }

    public Invitation findById(Long invitationId) {
        return invitationRepository.findById(invitationId)
                .orElseThrow(() -> new IllegalArgumentException("Invitation not found"));
    }

    public List<Invitation> findByRecipient(User currentUser) {
        return invitationRepository.findByRecipientId(currentUser.getId());
    }

    public boolean isUserPartOfGame(Long userId, Long gameId) {
        return invitationRepository.existsByRecipientIdAndGameIdAndAcceptedTrue(userId, gameId) ||
                invitationRepository.existsBySenderIdAndGameId(userId, gameId);
    }

    @Transactional
    public void acceptInvitation(Long invitationId, Long userId) {
        // Retrieve the invitation by its ID or throw an exception if not found
        Invitation invitation = invitationRepository.findById(invitationId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid invitation ID"));

        // Get the game from the invitation and retrieve the user by ID
        Game game = invitation.getGame();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID"));

        // Check if the user is already registered as a participant in the game
        boolean alreadyParticipant = participantRepository.existsByGameAndUser(game, user);
        if (!alreadyParticipant) {
            // Create and save a new participant for this game
            Participant participant = new Participant();
            participant.setGame(game);
            participant.setUser(user);
            participant.setScore(0); // Initial score
            participantRepository.save(participant);
        }

        // Mark the invitation as accepted
        invitation.setAccepted(true);
        invitationRepository.save(invitation);
    }
}
