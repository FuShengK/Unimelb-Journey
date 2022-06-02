package cribbage;

import ch.aplu.jcardgame.Card;

public class ScoringStarter {
    public int getScore(Card starter, IPlayer player) {
        int score = 0;
        final int JACK = 11;
        final int STARTER_JACK_POINT = 2;
        Logger log = Logger.getInstance();

        if (((Cribbage.Rank) starter.getRank()).order == JACK) {
            score += STARTER_JACK_POINT;
            player.setScore(player.getScore() + score);
            log.log(String.format("score,P%d,%d,%d,jack,%s", player, player.getScore(), score, starter));
        }
        return score;
    }
}


