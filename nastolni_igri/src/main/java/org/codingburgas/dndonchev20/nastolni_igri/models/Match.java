package org.codingburgas.dndonchev20.nastolni_igri.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "matches")
@Data
@NoArgsConstructor
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "game_id", nullable = false)
    private Game tournament;

    @ManyToOne
    @JoinColumn(name = "player1_id", nullable = false)
    private User player1;

    @ManyToOne
    @JoinColumn(name = "player2_id", nullable = false)
    private User player2;

    @Column(nullable = false)
    private Integer player1Score;

    @Column(nullable = false)
    private Integer player2Score;

    @Column(nullable = false)
    private String status = "PENDING"; // PENDING, COMPLETED

    @Column(nullable = true) // Null if match is a draw
    private Long winnerId;

    public Long getWinnerId() {
        if (player1Score > player2Score) {
            return player1.getId();
        } else if (player1Score < player2Score) {
            return player2.getId();
        } else {
            return null; // Draw
        }
    }


    public Match(Game gameById, User user, User user1, int score1, int score2, String completed) {
        this.tournament = gameById;
        this.player1 = user;
        this.player2 = user1;
        this.player1Score = score1;
        this.player2Score = score2;
        this.status = completed;
    }
}
