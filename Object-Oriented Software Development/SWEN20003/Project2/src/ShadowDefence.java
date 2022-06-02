import bagel.*;
import bagel.map.TiledMap;
import bagel.util.Point;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
/**
 * ShadowDefence, a tower defence game
 */
public class ShadowDefence extends AbstractGame {
    private static TiledMap map;
    private static final int BOUND = 0;
    private static final int WIDTH = 1024;
    private static final int HEIGHT = 768;
    private static final int ALL_INITIAL_VALUE = 0;
    private static final double ALL_INITIAL_DOUBLE_VALUE = 0.0;
    private static final int INITIAL_FOND = 500;
    private static final int INITIAL_LIFE = 25;
    private static final int WAVE_BONUS = 100;
    private static final int END_AWARD = 150;
    // we have only 3 types of towers
    private static final int NUM_TYPE_TOWERS = 3;
    private static final int PICKING_TANK = 0;
    private static final int PICKING_SUPER_TANK = 1;
    private static final int PICKING_PLANE = 2;
    private static final int NOT_PICKING = -1;
    // our maps and waves file string
    private static final String LEVEL1 = "res/levels/1.tmx";
    private static final String LEVEL2 = "res/levels/2.tmx";
    private static final String WAVES = "res/levels/waves.txt";
    // the set for check image bound on buy panel
    private static final int LEFT_OFF_SET = 64;
    private static final int BELOW = 80;
    private static final int GAP = 120;
    // a number of types of arraylist to store map stuff
    private static ArrayList<Slicer> slicers = new ArrayList<>();
    private static ArrayList<Tower> towers = new ArrayList<>();
    private static ArrayList<Projectile> projectiles = new ArrayList<>();
    private static ArrayList<Explosive> explosives = new ArrayList<>();
    private static int time; // frames
    private static final int MAX_TSCALE = 5;
    private static final int START_TSCALE = 1;
    private static int tScale = START_TSCALE;
    private static Player player;
    private Status status;
    private BuyPanel bPanel;
    private StatusPanel sPanel;
    private String[] levels = {LEVEL1, LEVEL2};
    private ArrayList<Wave> waves;
    private int curLevel;
    private int curWave;
    private int curEvent;
    private boolean waveStarted;
    private int pickItem = NOT_PICKING; // (-1, not yet) ,(0, tank), (1,super tank), (2,plane)
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
        super(WIDTH, HEIGHT, "ShadowDefence");
        // Initialize stuff for later use
        curLevel = ALL_INITIAL_VALUE;
        loadLevel();
        time = ALL_INITIAL_VALUE ;
        tScale = ALL_INITIAL_VALUE;
        waveStarted = false;
        player = Player.getInstance();
        status = Status.WAITING;
        sPanel = new StatusPanel();
        bPanel = new BuyPanel();
    }
    /**
     * Updates the game state approximately 60 times a second(fps = 60), potentially reading from input.
     * @param input The input instance which provides access to keyboard/mouse state information.
     */
    @Override
    protected void update(Input input) {
        map.draw(ALL_INITIAL_VALUE,ALL_INITIAL_VALUE,ALL_INITIAL_VALUE,ALL_INITIAL_VALUE,WIDTH, HEIGHT);
        theInput(input);
        // separate update methods to let error can be fix easier
        // and they are update with frames when game is running
        updateLevel();
        updateWave();
        updateSlicers();
        updateTowers();
        updateProjectiles();
        updateExplosives();
        updateStatus();
        // when we not picking (pick item = [0,1,2]), and the mouse is at valid area
        // then we can see the item attached on the mouse
        if(pickItem != NOT_PICKING && validating(input)){
            bPanel.getT(pickItem).getIM().draw(input.getMouseX(),input.getMouseY());
        }
        // Increase the frame counter by the current timescale
        time += tScale;
        // Close the game if we have no enough lives
        if(player.getL() <= ALL_INITIAL_VALUE){ Window.close();}
    }
    /**
     * To reset our game for next level
     */
    public void resetGame(){
        slicers.clear();
        towers.clear();
        projectiles.clear();
        explosives.clear();
        player.setF(INITIAL_FOND);
        player.setL(INITIAL_LIFE);
        waveStarted = false;
        status = Status.WAITING;
    }
    /**
     * isolate our input to make sure it run properly and find the error easily
     */
    public void theInput(Input input){
        // check if it is time to spawn and still have a number of wave to be set
        // and when a wave is finished then we can press again to start next wave
        if(!waveStarted && input.wasPressed(Keys.S) && curWave < waves.size()){
            waves.get(curWave).start();
            waveStarted = true;
            if(tScale == ALL_INITIAL_VALUE){ tScale = START_TSCALE;}

            // this for setting every towers to original direction
            for(Tower tower : towers){
                tower.reset();
            }
        }
        // Time Scale control
        if(input.wasPressed(Keys.K)){ if(tScale > START_TSCALE){ tScale--; }}
        if(input.wasPressed(Keys.L)){ if(tScale <= MAX_TSCALE){tScale++; }}
        // Mouse control
        if(input.wasPressed(MouseButtons.RIGHT)){ pickItem = NOT_PICKING; }
        if(input.wasPressed(MouseButtons.LEFT)){
            // check if this click pick any item
            if(pickItem == NOT_PICKING){
                pickItem = picks(input);
            } else {
                // check its validity when we pick
                // cannot on the panel, tower and blocked tilemap
                if(validating(input)){
                    // place the towers
                    setting(input);
                }
            }
        }
    }
    /**
     * Load levels and map to run the game
     */
    public void loadLevel(){
        map = new TiledMap(levels[curLevel]);
        // if have the level then run each wave
        loadWave();
    }
    /**
     * Load waves to know our events and time to spawn the slicers
     */
    public void loadWave(){
        // read file to load our game set
        try(BufferedReader br = new BufferedReader(new FileReader(WAVES))){
            String info = null;
            ArrayList<Wave> waves = new ArrayList<>();
            ArrayList<Event> events = new ArrayList<>();
            while (((info = br.readLine())) != null){
                String[] cells = info.split(",");
                if(cells[1].equals("spawn")){
                    // parse Data
                    int waveNum = Integer.parseInt(cells[0]);
                    int numSpawn = Integer.parseInt(cells[2]);
                    String typeSpawn = cells[3];
                    int delay = Integer.parseInt(cells[4]);
                    events.add(new SpawnEvent(waveNum, numSpawn, typeSpawn, delay));
                } else {
                    int waveNum = Integer.parseInt(cells[0]);
                    int delay = Integer.parseInt(cells[2]);
                    events.add(new DelayEvent(waveNum, delay));
                }
            }
            // add events into waves with same waveNum
            int i = 1;
            Wave w = new Wave(i);
            for (Event event : events){
                if (event.getWN() != i) {
                    waves.add(w);
                    i += 1;
                    w = new Wave(i);
                    w.getWE().add(event);
                }
                w.getWE().add(event);
            }
            waves.add(w);
            this.waves = waves;
            this.curWave = ALL_INITIAL_VALUE;
        } catch (IOException e) { e.printStackTrace(); }
    }
    /**
     * Load panel display on the map
     */
    public void loadPanel(){
        bPanel.render(ALL_INITIAL_DOUBLE_VALUE, ALL_INITIAL_DOUBLE_VALUE, player.getF());
        sPanel.render(ALL_INITIAL_DOUBLE_VALUE, HEIGHT - sPanel.getIM().getHeight(), curWave + 1, tScale, status, player.getL());
    }
    /**
     * To update levels
     */
    public void updateLevel() {
        if(curWave >= waves.size()){
            curLevel++;
            if(curLevel < levels.length){
                // if current level run out of all the waves
                // then move to next level and reset the game
                loadLevel();
                resetGame();
                tScale = START_TSCALE;
            }
        }
    }
    /**
     * To update waves
     */
    public void updateWave() {
        if (waveStarted) {
            Wave wave = waves.get(curWave);
            // Wave ended
            if (curEvent >= wave.getWE().size() && slicersOut()) {
                player.setF(player.getF() + END_AWARD + curWave*WAVE_BONUS);
                waveStarted = false;
                curWave++;
                curEvent = ALL_INITIAL_VALUE;
                slicers.clear();
            } else if (curEvent < wave.getWE().size()) {
                // process event
                Event E = wave.getWE().get(curEvent);
                // if event haven't started
                if (!E.isStarted()) { E.start(); }
                if (E instanceof SpawnEvent) {
                    // check if it is time to creat a slicer
                    // count < numSpawn && time gap == delay
                    SpawnEvent SE = (SpawnEvent) E;
                    int c = SE.getC();
                    if (c < SE.getNS() && ((time - SE.getST())) / (SE.getD() * 60 / 1000) == c) {
                        Slicer newS = null;
                        String ST = SE.getTS();
                        switch (ST) {
                            case "slicer":
                                newS = new RegularSlicer(map.getAllPolylines().get(0));
                                break;
                            case "superslicer":
                                newS = new SuperSlicer(map.getAllPolylines().get(0));
                                break;
                            case "megaslicer":
                                newS = new MegaSlicer(map.getAllPolylines().get(0));
                                break;
                            case "apexslicer":
                                newS = new ApexSlicer(map.getAllPolylines().get(0));
                                break;
                        }
                        slicers.add(newS);
                        // Update the count
                        SE.setC(SE.getC() + 1);
                    }
                }
                // Check if event ends or just go on next one
                if (time >= E.getET()) { curEvent++; }
            }
        }
    }
    /**
     * To update slicers
     */
    public void updateSlicers(){
        for(Slicer s : slicers){
            // if slicers are not gone or completed
            // then keep it update and render image to next move
            if(s.onMap()){
                s.update();
                s.render();
            }
        }
    }
    /**
     * To update Towers
     */
    public void updateTowers(){
        for (Tower t : towers) {
            t.update(slicers, projectiles);
            t.render();
            // if passive plane is in
            // then set the plane
            if(t instanceof Plane){
                Plane p = (Plane) t;
                p.update(explosives);
            }
        }
    }
    /**
     * To update projectiles
     */
    public void updateProjectiles(){
        for(Projectile p : projectiles){
            // if the slicers still on the map
            // and the projectile has attacked then keep tracing
            if(p.onMap()) {
                p.update(slicers);
                p.render();
            }
        }
    }
    /**
     * To update Explosives
     */
    public void updateExplosives(){
        for(Explosive e : explosives){
            e.update(slicers);
        }
    }
    /**
     * To update status
     */
    public void updateStatus(){
        // check current status then print it on the status panel
        status =  (!waveStarted) ?  Status.WAITING : Status.IN_PROGRESS;
        if(pickItem != NOT_PICKING){ status = Status.PLACING; }
        if(curWave >= waves.size() && curLevel >= levels.length){ status = Status.WIN; }
        loadPanel();
    }
    /**
     * To check if all slicers are either destroyed or exiting
     * @return boolean
     */
    public boolean slicersOut(){
        for(Slicer slicer : slicers){
            if(slicer.onMap())
                return false;
        }
        return true;
    }
    /**
     * To validate if the input is in agreed place
     * @param input the mouse
     * @return boolean
     */
    public boolean validating(Input input){
        int i;
        boolean first, second;
        // check if the mouse is on the buy panel or status panel
        for(i=0;i<NUM_TYPE_TOWERS;i++) {
            double SBX = bPanel.getIM().getWidth()/2;
            double bY = bPanel.getIM().getHeight()/2;
            double sY = HEIGHT - sPanel.getIM().getHeight()/2;
            first = bPanel.getIM().getBoundingBoxAt(new Point(SBX, bY)).intersects(input.getMousePosition());
            second = sPanel.getIM().getBoundingBoxAt(new Point(SBX,sY)).intersects(input.getMousePosition());
            if(first || second){ return false; }
        }
        // check if the tower is on the towers
        for(Tower t : towers){
            if(t.getRec().intersects(input.getMousePosition())){
                return false;
            }
        }
        // make sure the towers wont be set on the blocked map
        int x = (int) input.getMouseX();
        int y = (int) input.getMouseY();
        if(x>WIDTH || y>HEIGHT || x<BOUND || y<BOUND){ return false; }
        if(map.getPropertyBoolean(x, y, "blocked", false)){  return false; }
        return true;
    }
    /**
     * To buy the tower and make sure we have enough fond to pick it
     * @param input mouse
     * @return pickitem
     */
    public int picks(Input input) {
        int i, ans = NOT_PICKING;
        // if the mouse is on the right position
        // them we can pick the item
        for(i=0;i<NUM_TYPE_TOWERS;i++) {
            double bX = LEFT_OFF_SET + GAP*i;
            double bY = BELOW - bPanel.getT(i).getIM().getHeight() / 2;
            if(bPanel.getT(i).getIM().getBoundingBoxAt(new Point(bX,bY)).intersects(input.getMousePosition())){
                ans = i;
            }
        }
        // if our money is less than the item price then we cannot pick it
        if(ans != NOT_PICKING && player.getF() < bPanel.getT(ans).getP()){ ans = NOT_PICKING; }
        return ans;
    }
    /**
     * To set our tower on the map
     * @param input mouse
     */
    public void setting(Input input){
        Point loc = input.getMousePosition();
        // check our picking type
        Tower newT = null;
        if(pickItem == PICKING_TANK){
            newT = new Tank(loc);
        } else if (pickItem == PICKING_SUPER_TANK){
            newT = new SuperTank(loc);
        } else if (pickItem == PICKING_PLANE){
            newT = new Plane(loc);
        }
        // add the picking item to our waiting list and cost that purchase
        towers.add(newT);
        player.setF(player.getF()- bPanel.getT(pickItem).getP());
        // turn back to not pick when we placed the tower
        pickItem = NOT_PICKING;
    }
    /*Getter and Setter*/
    public static int getWIDTH() { return WIDTH; }
    public static int getHEIGHT() { return HEIGHT; }
    public static int getT() { return time; }
    public static int getTS() { return tScale; }
    public static Player getPlayer() { return player; }
}
