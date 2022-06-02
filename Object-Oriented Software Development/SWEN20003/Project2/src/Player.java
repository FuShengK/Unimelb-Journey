/**
 * a class for player
 */
public class Player {
    private static final int START_FOND = 500;
    private static final int START_LIFE = 25;
    private int fond;
    private int lives;
    private static Player instance;
    /**
     * set up player
     */
    private Player(){ fond = START_FOND; lives = START_LIFE;}
    /**
     * to let player get player mode
     * @return return new player mode
     */
    public static Player getInstance(){ if(instance == null){ instance = new Player(); } return instance; }
    /*Getter and setter*/
    public int getF(){ return fond; }
    public void setF(int fond){ this.fond = fond; }
    public int getL(){ return lives; }
    public void setL(int lives){ this.lives = lives;}
}
