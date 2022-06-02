package cribbage;

import ch.aplu.jcardgame.Card;

import java.util.ArrayList;

public class PlayRuns extends IScoringSystem {

    @Override
    public int getScore() {

        Cribbage.Segment s = segment.copy();
        ArrayList<Card[]> cards;
        int score = 0;
        final int EMPTY = 0;

        while (s.segment.getNumberOfCards() > EMPTY) {
            int len = s.segment.getNumberOfCards();
            cards = s.segment.getSequences(len);
            if (cards.size() != EMPTY) {
                // make score sync to the card length
                score += len;
                player.setScore(player.getScore() + score);
                logger.log(String.format("score,P%d,%d,%d,run%d", s.lastPlayer, player.getScore(), score, len));
                break;
            }
            s.segment.removeFirst(false);
        }
        return score;
    }
}
