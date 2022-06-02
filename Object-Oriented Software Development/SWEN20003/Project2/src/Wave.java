import java.util.ArrayList;
/**
 * a class for waves
 */
public class Wave {
    private static final int START = 0;
    private int waveNum;
    private ArrayList<Event> Events;
    private int current;
    private int startT;
    /**
     * set up wave constructors
     * @param waveNum wave number
     */
    public Wave(int waveNum){
        setWN(waveNum);
        setWE(new ArrayList<>());
        current = START;
    }
    /**
     * to get the start time of wave
     */
    public void start(){ startT = ShadowDefence.getT(); }
    /*Getter and Setter*/
    public void setWN(int waveNum){ this.waveNum = waveNum; }
    public ArrayList<Event> getWE(){ return Events; }
    public void setWE(ArrayList<Event> Events){ this.Events = Events; }
}
