/**
 * ta class for spawn event
 */
public class SpawnEvent extends Event{
    private static final int START_COUNT = 0;
    private int numSpawn;
    private String typeSpawn;
    private int delay;
    private int count;
    /**
     * set up spawn event
     * @param waveNumber wavenumber
     * @param numSpawn number of spawn
     * @param typeSpawn type to spawn
     * @param delay delay time
     */
    public SpawnEvent(int waveNumber, int numSpawn, String typeSpawn, int delay){
        //Constructor
        super(waveNumber);
        setNS(numSpawn);
        setTS(typeSpawn);
        setD(delay);
        setC(START_COUNT);
    }
    /**
     * A method to know when to start and when to end
     */
    public void start(){
        setST(ShadowDefence.getT());
        int duration = (getNS() - 1) * (getD() * 60 / 1000);
        setET(getST() + duration);
        setS(true);
    }
    /*Getter and Setter*/
    public int getNS(){ return numSpawn; }
    public void setNS(int numSpawn){ this.numSpawn = numSpawn; }
    public String getTS(){ return typeSpawn; }
    public void setTS(String typeSpawn){ this.typeSpawn = typeSpawn; }
    public int getD(){ return delay; }
    public void setD(int delay){ this.delay = delay; }
    public int getC(){ return count; }
    public void setC(int count){ this.count = count; }
}
