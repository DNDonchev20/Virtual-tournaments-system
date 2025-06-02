package org.codingburgas.dndonchev20.nastolni_igri.utils;

/**
 * A very simple algorithm that randomly determines a match result.
 * If Math.random() < 0.5 then Player 1 wins (3 points for win, 0 for loss),
 * otherwise Player 2 wins (0 for loss, 3 for win).
 *
 * @return an array of two integers: [points for player1, points for player2]
 */
public class TournamentRankingUtil {
    public static int[] simpleTournamentResult() {
        return Math.random() < 0.5 ? new int[] {3, 0} : new int[] {0, 3};
    }
}
