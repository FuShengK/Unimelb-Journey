package cribbage;

public class ShowComposite extends ScoringComposite {
    @Override
    public int getScore() {
        int score = 0;
        for (IScoringSystem scoring : this.scoringList) { score += scoring.getScore(); }
        return score;
    }
}
