package cribbage;

import java.util.ArrayList;

public abstract class ScoringComposite extends IScoringSystem {

    protected ArrayList<IScoringSystem> scoringList = new ArrayList<>();

    public void add(IScoringSystem scoring) {
        scoringList.add(scoring);
    }
    // Set players and segments for score purposes
    public void setPlayer(IPlayer player) {
        for (IScoringSystem scoring : this.scoringList) {
            scoring.setPlayer(player);
        }
    }
    public void setSegment(Cribbage.Segment segment) {
        for (IScoringSystem scoring : this.scoringList) {
            scoring.setSegment(segment);
        }
    }
    @Override
    public abstract int getScore();
}
