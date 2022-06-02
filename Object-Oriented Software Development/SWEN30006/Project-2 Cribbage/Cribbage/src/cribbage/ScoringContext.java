package cribbage;

public class ScoringContext {
    private final IScoringSystem scoring;

    public ScoringContext(IScoringSystem scoring) {
        this.scoring = scoring;
    }
    public int getScore() {
        return this.scoring.getScore();
    }
    public void setSegment(Cribbage.Segment s) {
        scoring.setSegment(s);
    }
    public void setPlayer(IPlayer player) {
        scoring.setPlayer(player);
    }
}
