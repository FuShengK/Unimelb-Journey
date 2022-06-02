import bagel.DrawOptions;
import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;
import bagel.util.Vector2;
import java.util.ArrayList;
/**
 * a class towers
 */
public class Tower {
    private final int START = 0;
    private final int FRAME_NUM = 60;
    private final int MILISECOND_TO_SECOND = 1000;
    private final int LAST_FIRE_INITIAL_SET = -1;
    private final double INITIAL_ROTATION = 0.0;
    private final int PROJECTILE_SPEED = 10;
    private final int TANK_PROJECTILE_DAMAGE = 1;
    private final int SUPER_TANK_PROJECTILE_DAMAGE = 3;
    private Point location;
    private int radius;
    private Image IM;
    private Rectangle rec;
    private int CD;
    private int damage;
    private Slicer target;
    private double rotation;
    private int lastFire;
    private int count;
    /**
     * set up tower constructor
     * @param location mouse
     */
    public Tower(Point location, String image){
        setIM(new Image(image));
        setLoc(location);
        setT(null);
        setRotat(INITIAL_ROTATION);
        setLF(LAST_FIRE_INITIAL_SET);
        this.count = START;
    }
    /**
     * update tower with slicers and projectiles
     * @param slicers target
     * @param projectiles our weapon
     */
    public void update(ArrayList<Slicer> slicers, ArrayList<Projectile> projectiles){
        // find new target if previous target our of range
        setT(null);
        // select target
        if(getT() == null){ trace(slicers); }
        // face the target
        if(getT() != null){
            count += ShadowDefence.getTS();
            setRotat(turn(getLoc().asVector(), getT().getLoc().asVector()));
            // when the cool down finished then load projectiles
            if(count >= (CD * FRAME_NUM / MILISECOND_TO_SECOND)){
                projectiles.add(fireType());
                count = START;
            }
        }
    }
    /**
     * to launch attack to slicers
     * @param slicers target
     */
    public void trace(ArrayList<Slicer> slicers){
        for(Slicer s: slicers){
            // if target in our attacking range then trace them
            if(s.onMap() && in(s)){
                setT(s);
                return;
            }
        }
    }
    /**
     * To check if slicers are in the attack range
     * @param slicer target
     * @return boolean
     */
    public boolean in(Slicer slicer){
        double x = Math.abs(slicer.getLoc().x - getLoc().x);
        double y = Math.abs(slicer.getLoc().y - getLoc().y);
        return x <= getR() && y <= getR();
    }
    /**
     * Initialize the tower
     * @param r radius
     * @param CD cool down
     * @param d damage
     */
    public void setTower(int r, int CD, int d){
        setR(r);
        setCD(CD);
        setD(d);
    }
    /**
     * render tower on the map
     */
    public void render(){
        IM.draw(location.x, location.y, new DrawOptions().setRotation(getRotat()));
    }
    /**
     * check tower type and create new projectile
     * @return the projectile type
     */
    public Projectile fireType(){
        Projectile p = null;
        // check its tower type
        if(this instanceof Tank){
            p = new TankProjectile(getLoc(), getT());
            p.setType(TANK_PROJECTILE_DAMAGE,PROJECTILE_SPEED);
        }
        if(this instanceof SuperTank){
            p = new SuperTankProjectile(getLoc(), getT());
            p.setType(SUPER_TANK_PROJECTILE_DAMAGE,PROJECTILE_SPEED);
        }
        target = null;
        return p;
    }
    /**
     * to reset the tower
     */
    public void reset(){
        setT(null);
        setLF(LAST_FIRE_INITIAL_SET);
        setRotat(INITIAL_ROTATION);
    }
    /**
     * turn the tower direction to fire
     * @param g goal
     * @param l current location
     * @return angle
     */
    public double turn(Vector2 g, Vector2 l){ return Math.atan2(l.y - g.y, l.x - g.x) + 1.57; }
    /*Getter and Setter*/
    public Image getIM() { return IM; }
    public void setIM(Image IM) { this.IM = IM; }
    public Point getLoc() { return location; }
    public void setLoc(Point location) { this.location = location; }
    public int getR() { return radius; }
    public void setR(int radius) { this.radius = radius; }
    public Rectangle getRec() { return rec; }
    public void setRec(Rectangle rec) { this.rec = rec; }
    public void setCD(int CD) { this.CD = CD; }
    public Slicer getT() { return target; }
    public void setT(Slicer target) { this.target = target; }
    public double getRotat() { return rotation; }
    public void setRotat(double rotation) { this.rotation = rotation; }
    public void setLF(int lastFire) { this.lastFire = lastFire; }
    public void setD(int damage) { this.damage = damage; }
}
