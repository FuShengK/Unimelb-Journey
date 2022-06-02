/**
 * delay event class stands for the event with delay time
 */
public class DelayEvent extends Event{
    private int delay;

    /**
     * set up delay event
     * @param waveNumber wave number
     * @param delay delay time
     */
    public DelayEvent(int waveNumber, int delay){
        super(waveNumber);
        setDelay(delay);
    }
    /**
     * start the event with spawn delay
     */
    public void start(){
        // load the start time and wait delay finished then end
        setST(ShadowDefence.getT());
        int duration = getDelay() * 60 / 1000;
        setET(getST() + duration);
        setS(true);
    }
    /*Getter and setter*/
    public int getDelay(){ return delay; }
    public void setDelay(int delay){ this.delay = delay; }
}
