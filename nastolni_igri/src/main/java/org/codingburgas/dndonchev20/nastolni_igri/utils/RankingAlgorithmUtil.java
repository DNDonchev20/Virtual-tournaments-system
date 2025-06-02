package org.codingburgas.dndonchev20.nastolni_igri.utils;

/**
 * A simple Elo rating algorithm implementation.
 * Given two players’ current ratings and the outcome for player A
 * (1.0 = win, 0.0 = loss, 0.5 = draw), calculates new Elo ratings.
 */
public class RankingAlgorithmUtil {
    // K-factor determines how fast ratings change.
    private static final int K = 32;

    /**
     * Updates the Elo ratings for two players.
     *
     * @param ratingA current rating of player A
     * @param ratingB current rating of player B
     * @param scoreA  result for player A (1.0 = win, 0.0 = loss, 0.5 = draw)
     * @return an array of new ratings: [newRatingA, newRatingB]
     */
    public static double[] updateEloRatings(double ratingA, double ratingB, double scoreA) {
        double expectedA = 1.0 / (1 + Math.pow(10, (ratingB - ratingA) / 400.0));
        double expectedB = 1.0 - expectedA;
        double newRatingA = ratingA + K * (scoreA - expectedA);
        double newRatingB = ratingB + K * ((1.0 - scoreA) - expectedB);
        return new double[]{ newRatingA, newRatingB };
    }
}
