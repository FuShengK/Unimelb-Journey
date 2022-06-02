package cribbage;

import ch.aplu.jcardgame.Card;

public class ShowJack extends IScoringSystem {

    @Override
    public int getScore() {


        int score = 0;
        final int JACK = 11;
        final int SAME_STARTER_JACK_POINT = 1;

        // Get Starter
        Cribbage.Segment seg = segment.copy();
        Card starter = seg.segment.getLast();
        seg.segment.removeLast(false);

        // Check Jack
        for (Card the_card : seg.segment.getCardList()) {
            if (((Cribbage.Rank) the_card.getRank()).order == JACK) {
                if (the_card.getSuitId() == starter.getSuitId()) {
                    score += SAME_STARTER_JACK_POINT;
                    player.setScore(player.getScore() + SAME_STARTER_JACK_POINT);
                    logger.log(String.format("score,P%d,%d,%d,jack,%s", seg.lastPlayer, player.getScore(), score, Cribbage.canonical(the_card)));
                }
            }
        }
        return score;
    }
}
