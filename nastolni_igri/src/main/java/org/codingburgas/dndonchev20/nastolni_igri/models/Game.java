package org.codingburgas.dndonchev20.nastolni_igri.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "games")
@Data
@NoArgsConstructor
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id", nullable = false)
    private User creator;

    @Column(nullable = false)
    private boolean isTournament = false; // Indicates if the game is a tournament

    @Column
    private String tournamentFormat; // "knockout" or "round-robin"

    @Column(nullable = false)
    private String status = "OPEN"; // OPEN, IN_PROGRESS, COMPLETED

    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Participant> participants = new ArrayList<>();


    public void setState(String s) {

    }
}
