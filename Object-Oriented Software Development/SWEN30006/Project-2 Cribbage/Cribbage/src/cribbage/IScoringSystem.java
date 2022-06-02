package cribbage;

public abstract class IScoringSystem {

    static Logger logger = Logger.getInstance();
    /*
      We now have player to play the game and can make log display, and have play state and show state
    */
    public Cribbage.Segment segment;
    IPlayer player = null;

    public Cribbage.Segment getS() {
        return segment;
    }
    public void setSegment(Cribbage.Segment segment) {
        this.segment = segment;
    }
    public IPlayer getPlayer() {
        return player;
    }
    public void setPlayer(IPlayer player) {
        this.player = player;
    }
    public abstract int getScore();
}
