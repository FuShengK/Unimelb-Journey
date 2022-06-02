import bagel.AbstractGame;
import bagel.Input;
import bagel.Keys;
import bagel.Window;
import bagel.map.TiledMap;
import bagel.util.Point;

public class ShadowDefence extends AbstractGame {

    /*Initial attributes*/
    private final TiledMap map;
    private Boolean hasStarted = false;
    private final int startRate = 1;
    private int speedRate = startRate;
    private int time = 0;
    private int numCount = 1;
    private final int maxNum = 5;
    private Slicer[] slicers = new Slicer[maxNum];
    private boolean missionCompleted = false;

    /**
     * Entry point for Bagel game
     * */
    public static void main(String[] args) {
        // Create new instance of game and run it
        new ShadowDefence().run();
    }
    /**
     * Setup the game
     * */
    public ShadowDefence(){
        // Constructor
        int i;
        map = new TiledMap("res/levels/1.tmx");

        /*To load the map with its polylines and get roads to run*/
        int numRoads = map.getAllPolylines().get(0).size();
        Itinerary[] roads = new Itinerary[numRoads];
        for (i = 1; i < numRoads; i++){
            Point xP = map.getAllPolylines().get(0).get(i - 1);
            Point yP = map.getAllPolylines().get(0).get(i);
            roads[i - 1] = new Itinerary(xP, yP);
        }

        /*Make each slicer to run on the road*/
        for (i = 0; i < maxNum; i++){
            slicers[i] = new Slicer(roads);
        }
    }

    /**
     * Updates the game state approximately 60 times a second(fps = 60), potentially reading from input.
     * @param input The input instance which provides access to keyboard/mouse state information.
     */
    @Override
    protected void update(Input input) {
        map.draw(0,0,0,0,Window.getWidth(),Window.getHeight());
        if (input.isDown(Keys.S)) hasStarted = true;

        int i;
        if (hasStarted){
            //Get fast
            if(input.isDown(Keys.L)) speedRate++;
            //Get slow
            if(input.isDown(Keys.K) && speedRate > 1) speedRate--;

            if(!missionCompleted){
                //Run each slicers
                for (i = 0; i < numCount; i++) {
                    slicers[i].startGoing(speedRate);
                }

                /*Spawning the slicers based on delay time and fps(frames per second)*/
                int delay = 5;
                int fps = 60;
                if (time >= delay * fps && numCount < maxNum){
                    numCount++;
                    time = 0;
                }
            } else {
                Window.close();
            }

            /*Make slicers run and programme finished if all of them reach the end point*/
            for (i = 0; i < maxNum; i++){
                if (!slicers[i].isDown()){
                    missionCompleted = false;
                    break;
                } else {
                    missionCompleted = true;
                }
            }
            time += speedRate;
        }

    }
}
