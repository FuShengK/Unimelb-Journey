import bagel.DrawOptions;
import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;
import bagel.util.Vector2;
import java.util.List;
/**
 * slicer super class and to setup all the need for slicers
 */
public class Slicer {

    private final List<Point> polyLine;
    private final int START = 0;
    private Image theImage;
    private boolean completed;
    private boolean gone;
    private double speed;
    private double rotation;
    // setting for slicers
    private int penalty;
    private int reward;
    private int HP;
    private int PLIndex;
    private Point location;
    private Point goal;
    private Vector2 velocity;
    private Rectangle rec;
    /**
     * slicer at start point of line
     * @param polyLine load polyline to run
     */
    public Slicer(List<Point> polyLine, String pic) {
        // Constructors
        this.theImage = new Image(pic);
        this.polyLine = polyLine;
        this.PLIndex = START;
        this.location = polyLine.get(PLIndex);
        this.goal = polyLine.get(PLIndex + 1);
        this.completed = false;
        this.gone = false;
        this.velocity = goal.asVector().sub(location.asVector()).normalised();
        this.rotation = turn(location.asVector(), goal.asVector());
    }
    /**
     * when the slicer is gone then use this constructor for their children
     * @param parent the parent
     */
    public Slicer(Slicer parent){
        this.polyLine = parent.polyLine;
        this.PLIndex = parent.PLIndex;
        this.location = parent.location;
        this.goal = parent.goal;
        this.completed = parent.completed;
        this.velocity = parent.velocity;
        this.rotation = parent.rotation;
    }
    /**
     * Update slicers on the map and make sure they are running on the polyline
     */
    public void update(){
        // if not completed the map then going
        if(!completed){
            if(goal.distanceTo(location) < ShadowDefence.getTS() * speed){
                PLIndex++;
                if( PLIndex < polyLine.size()){
                    goal = polyLine.get(PLIndex);
                    velocity = goal.asVector().sub(location.asVector()).normalised();
                    this.rotation = turn(location.asVector(), goal.asVector());
                } else {
                    completed = true;
                    ShadowDefence.getPlayer().setL(ShadowDefence.getPlayer().getL() - penalty);
                    return;
                }
            }
            this.location = this.location.asVector().add(velocity.mul(ShadowDefence.getTS() * speed)).asPoint();
            this.rec = getIM().getBoundingBoxAt(location);
        }
    }
    /**
     * slicers set up pack
     * @param HP slicers life
     * @param speed speed
     * @param p penalty
     * @param r reward
     */
    public void setSlicer(int HP, double speed, int p, int r){
        setHP(HP);
        setSpeed(speed);
        setP(p);
        setR(r);
    }
    /**
     * render slicer on the map
     */
    public void render(){ getIM().draw(location.x, location.y, new DrawOptions().setRotation(rotation)); }
    /**
     * turn the direction of slilcers
     * @param g goal
     * @param l current location
     * @return the angle
     */
    public double turn(Vector2 g, Vector2 l){ return Math.atan2(l.y - g.y, l.x - g.x); }
    /**
     * check if slicers are on the map
     * @return boolean
     */
    public boolean onMap(){ return !isCompleted() && !isGone(); }
    /*Getter and Setter*/
    public Image getIM() { return theImage; }
    public void setIM(Image theImage) { this.theImage = theImage; }
    public boolean isCompleted() { return completed; }
    public boolean isGone() { return gone; }
    public void setGone(boolean gone) { this.gone = gone; }
    public void setSpeed(double speed) { this.speed = speed; }
    public void setP(int penalty) { this.penalty = penalty; }
    public int getR() { return reward; }
    public void setR(int reward) { this.reward = reward; }
    public int getHP() { return HP; }
    public void setHP(int HP) { this.HP = HP; }
    public Point getLoc() { return location; }
    public Rectangle getRec() { return rec; }
    public void setRec(Rectangle rec) { this.rec = rec; }
}
