package org.codingburgas.dndonchev20.nastolni_igri.models;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "tournament_participants")
@Data
public class Participant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "game_id", nullable = false)
    private Game game;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private int score = 0;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public void updateScore(int points) {
        this.score += points;
    }
}
