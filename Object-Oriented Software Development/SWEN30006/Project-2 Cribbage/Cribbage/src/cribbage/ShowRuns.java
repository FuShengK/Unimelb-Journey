package cribbage;

import ch.aplu.jcardgame.Hand;

public class ShowRuns extends IScoringSystem {

    @Override
    public int getScore() {

        Cribbage.Segment s = segment.copy();
        int score = 0;
        final int RUN_FIVE = 5;
        final int RUN_TWO = 2;
        final int EMPTY = 0;

        for (int runPoints = RUN_FIVE; runPoints > RUN_TWO; runPoints--) {
            Hand[] runs = s.segment.extractSequences(runPoints);
            if (runs.length > EMPTY) {
                for (Hand run : runs) {
                    score += runPoints;
                    player.setScore(player.getScore() + runPoints);
                    logger.log(String.format("score,P%d,%d,%d,run%d,%s", s.lastPlayer, player.getScore(), score, runPoints, Cribbage.canonical(run)));
                }
                break;
            }
        }
        return score;
    }
}
