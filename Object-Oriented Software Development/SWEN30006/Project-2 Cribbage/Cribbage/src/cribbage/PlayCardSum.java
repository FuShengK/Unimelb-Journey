package cribbage;

public class PlayCardSum extends IScoringSystem {
    @Override
    public int getScore() {

        Cribbage.Segment seg = segment.copy();
        int score = 0;
        final int CARD_SUM_THIRTYONE = 31;
        final int CARD_SUM_FIFTEEN = 15;
        final int THIRTYONE_POINT = 2;
        final int FIFTEEN_POINT = 2;
        final int GO_POINT = 1;

        int total = Cribbage.total(seg.segment);
        if (total == CARD_SUM_THIRTYONE) {
            // make score sync to the end point
            score += THIRTYONE_POINT;
            player.setScore(player.getScore() + score);
            logger.log(String.format("score,P%d,%d,%d,thirtyone", seg.lastPlayer, player.getScore(), score));
        } else if (total == CARD_SUM_FIFTEEN) {
            // make score sync if total == 15
            score += FIFTEEN_POINT;
            player.setScore(player.getScore() + score);
            logger.log(String.format("score,P%d,%d,%d,fifteen", seg.lastPlayer, player.getScore(), score));
        } else if (seg.go && seg.newSegment) {
            // make score sync to the card if someone say "go"
            score += GO_POINT;
            player.setScore(player.getScore() + score);
            logger.log(String.format("score,P%d,%d,%d,go", seg.lastPlayer, player.getScore(), score));
        }

        return score;
    }
}
