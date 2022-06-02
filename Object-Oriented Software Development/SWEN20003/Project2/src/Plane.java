import bagel.DrawOptions;
import bagel.util.Point;
import bagel.util.Rectangle;
import bagel.util.Vector2;
import java.util.ArrayList;
/**
 * plane class is for emergence where the tower cannot handle the slicers
 */
public class Plane extends Tower{
    private static final String IMAGE = "res/images/airsupport.png";
    // know the range of map and know the end
    private static final double WIDTH = 1024;
    private static final double HEIGHT = 768;
    private static final double INITIAL_DIRECTION = 0.0;
    private static final double GO_RIGHT = 1;
    private static final double GO_DOWN = 1;
    private static final double TURN_RIGHT = 1.57;
    private static final double TURN_DOWN = 3.14;
    private static final int FLY_SPEED = 3;
    private static final int TIME_BOUND = 2; // the drop time chosen from 1 to 2
    private static final int START = 0;
    private static final int LEFT_START = 0;
    private static final int TOP_START = 1;
    private static final int FRAMES = 60;
    private Point location;
    private double speed;
    private Vector2 velocity;
    // keep tracking the direction from last time
    private static int checkDir = 0;
    private double rotation;
    private Rectangle rec;
    private boolean completed;
    private int delayDrop;
    private int lastDrop;
    /**
     * set up plane
     * @param putPlace the point we click
     */
    public Plane(Point putPlace){
        super(putPlace, IMAGE);
        // when direction is 0 then from left to right
        // is 1 then from top to bottom and keep changing from last time
        if( getDir() == LEFT_START ){
            setUp(new Point(START, putPlace.y), new Vector2(GO_RIGHT, INITIAL_DIRECTION), TOP_START, TURN_RIGHT);
        } else {
            setUp(new Point(putPlace.x, START), new Vector2(INITIAL_DIRECTION, GO_DOWN), LEFT_START, TURN_DOWN);
        }
        setSpeed(FLY_SPEED);
        setCompleted(false);
        this.delayDrop = ((int)(Math.random() * TIME_BOUND) + 1) * FRAMES;
        this.lastDrop = START;
    }
    /**
     * update plane with its location and explosives
     * @param explosives to append in explosives arraylist
     */
    public void update(ArrayList<Explosive> explosives){
        if(this.location.x > WIDTH || this.location.y > HEIGHT){ this.completed = true; }
        setLoc(location.asVector().add(velocity.mul(ShadowDefence.getTS() * speed)).asPoint());
        if(!completed){
            lastDrop += ShadowDefence.getTS();
            if(lastDrop >= delayDrop){
                explosives.add(drop());
                lastDrop = START;
                delayDrop = ((int)(Math.random() * TIME_BOUND) + 1) * FRAMES;
            }
            setRec(getIM().getBoundingBoxAt(location));
        }
    }
    /**
     * render plane on the map
     */
    public void render(){getIM().draw(getLoc().x,getLoc().y, new DrawOptions().setRotation(getRotat()));}
    /**
     * to load new explosive for later drop
     * @return a new explosive
     */
    public Explosive drop(){ return new Explosive(location); }
    /**
     * setup pack
     * @param loc location
     * @param v velocity
     * @param dir direction
     * @param rot rotation
     */
    public void setUp(Point loc, Vector2 v, int dir, double rot){
        setLoc(loc);
        setV(v);
        setDir(dir);
        setRotat(rot);
    }
    /*Getter and setter*/
    public Point getLoc() { return location; }
    public void setLoc(Point location) { this.location = location; }
    public void setSpeed(double speed) { this.speed = speed; }
    public void setV(Vector2 velocity) { this.velocity = velocity; }
    public static int getDir() { return checkDir; }
    public static void setDir(int dir) { Plane.checkDir = dir; }
    public double getRotat() { return rotation; }
    public void setRotat(double rotation) { this.rotation = rotation; }
    public void setCompleted(boolean completed) { this.completed = completed; }
}
