package cribbage;

import ch.aplu.jcardgame.Hand;

public class PlaySames extends IScoringSystem {

    @Override
    public int getScore() {

        Cribbage.Segment s = segment.copy();
        int score = 0;
        int len;
        boolean getSames = false;
        final int EMPTY = 0;
        final int TWO_SAME = 2;
        final int THREE_SAME = 3;
        final int FOUR_SAME = 4;
        final int SAME_TWO_POINT = 2;
        final int SAME_THREE_POINT = 6;
        final int SAME_FOUR_POINT = 12;

        while ((len = s.segment.getNumberOfCards()) > EMPTY) {
            // while playing, it will check from longest to avoid double-checking
            Hand[] pairs;
            if (len == FOUR_SAME) {
                pairs = s.segment.extractQuads();
                // get 12 if 4 card in the same number
                if (pairs.length != EMPTY) {
                    getSames = true;
                    score += SAME_FOUR_POINT;
                    player.setScore(player.getScore() + SAME_FOUR_POINT);
                    break;
                }
            } else if (len == THREE_SAME) {
                pairs = s.segment.extractTrips();
                // get 6 if 3 card in the same number
                if (pairs.length != EMPTY) {
                    getSames = true;
                    score += SAME_THREE_POINT;
                    player.setScore(player.getScore() + SAME_THREE_POINT);
                    break;
                }
            } else if (len == TWO_SAME) {
                pairs = s.segment.extractPairs();
                // get 2 if 2 card in the same number
                if (pairs.length != EMPTY) {
                    getSames = true;
                    score += SAME_TWO_POINT;
                    player.setScore(player.getScore() + SAME_TWO_POINT);
                    break;
                }
            }
            s.segment.removeFirst(true);
        }

        if (getSames) { logger.log(String.format("score,P%d,%d,%d,pair%d", s.lastPlayer, player.getScore(), score, len)); }
        return score;
    }
}
