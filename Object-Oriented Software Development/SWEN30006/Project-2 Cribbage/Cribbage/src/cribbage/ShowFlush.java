package cribbage;

import ch.aplu.jcardgame.Card;

public class ShowFlush extends IScoringSystem {

    @Override
    public int getScore() {

        Cribbage.Segment seg = segment.copy();
        int score = 0;
        final int SAME_SUIT_CARDS = 4;
        final int SAME_SUIT_CARDS_POINTS = 1;

        // take out starter
        Card starter = seg.segment.getLast();
        seg.segment.removeLast(false);

        // check flush
        for (Cribbage.Suit suit : Cribbage.Suit.values()) {
            int num = seg.segment.getNumberOfCardsWithSuit(suit);
            if (num == SAME_SUIT_CARDS) {
                score = SAME_SUIT_CARDS;
                // Check starter
                if (starter.getSuit() == suit) {
                    score += SAME_SUIT_CARDS_POINTS;
                    seg.segment.insert(starter, false);
                    break;
                }
                player.setScore(player.getScore() + score);
                logger.log(String.format("score,P%d,%d,%d,flush%d", seg.lastPlayer, player.getScore(), score, score, Cribbage.canonical(seg.segment)));
                break;
            }
        }
        return score;
    }
}
