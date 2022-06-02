/**
 * a class for game events
 */
public class Event {
    private int waveNumber;
    private int startTime;
    private int endTime;
    private boolean started;
    private static final int DURATION = 0;
    /**
     * set up event
     * @param waveNumber wave number
     */
    public Event(int waveNumber){
        // Constructor
        this.waveNumber = waveNumber;
        started = false;
    }
    /**
     * start the event
     */
    public void start() {
        // set up start time and wait duration for end time
        setST(ShadowDefence.getT());
        setET(getST() + DURATION);
        setS(true);
    }
    /*Getter and setter*/
    public int getST(){ return startTime; }
    public int getET(){ return endTime; }
    public void setST(int startTime){ this.startTime = startTime; }
    public void setET(int endTime){ this.endTime = endTime; }
    public int getWN(){ return waveNumber; }
    public boolean isStarted(){ return started; }
    public void setS(boolean started){ this.started = started; }
}
