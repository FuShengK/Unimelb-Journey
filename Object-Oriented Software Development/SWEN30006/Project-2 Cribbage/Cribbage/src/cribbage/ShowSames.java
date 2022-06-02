package cribbage;

import ch.aplu.jcardgame.Hand;

public class ShowSames extends IScoringSystem {
    @Override
    public int getScore() {

        Cribbage.Segment seg = segment.copy();
        int score = 0;
        final int SAME_TWO_POINT = 2;
        final int SAME_THREE_POINT = 6;
        final int SAME_FOUR_POINT = 12;

        // Show same cards in different amount of identical number
        Hand[] pairs = seg.segment.extractPairs();
        for (Hand pair : pairs) {
            score += SAME_TWO_POINT;
            player.setScore(player.getScore() + SAME_TWO_POINT);
            logger.log(String.format("score,P%d,%d,%d,pair2,%s", seg.lastPlayer, player.getScore(), score, Cribbage.canonical(pair)));
        }

        seg = segment.copy();
        Hand[] trips = seg.segment.extractTrips();
        for (Hand trip : trips) {
            score += SAME_THREE_POINT;
            player.setScore(player.getScore() + SAME_THREE_POINT);
            logger.log(String.format("score,P%d,%d,%d,pair3,%s", seg.lastPlayer, player.getScore(), score, Cribbage.canonical(trip)));
        }

        seg = segment.copy();
        Hand[] quads = seg.segment.extractQuads();
        for (Hand quad : quads) {
            score += SAME_FOUR_POINT;
            player.setScore(player.getScore() + SAME_FOUR_POINT);
            logger.log(String.format("score,P%d,%d,%d,pair4,%s", seg.lastPlayer, player.getScore(), score, Cribbage.canonical(quad)));
        }
        return score;
    }
}
