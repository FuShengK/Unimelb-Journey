import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;
import bagel.util.Vector2;
import java.util.ArrayList;
/**
 * a class for projectile
 */
public class Projectile {
    // when the ApexSlicer is die then split 4 MegaSlicers
    private static final int MEGA_SPLIT = 4;
    private static final int PROJECTILE_SPEED = 10;
    private Image image;
    private Rectangle rec;
    private Point location;
    private Slicer target;
    private int speed;
    private Vector2 velocity; // Direction
    private boolean hit;
    private int damage;
    /**
     * projectile constructor
     * @param location location we click
     * @param target slicers
     */
    public Projectile(Point location, Slicer target, String IM){
        setI(new Image(IM));
        setLoc(location);
        setT(target);
        setS(PROJECTILE_SPEED);
        setV(target.getLoc().asVector().sub(location.asVector()).normalised());
        setHit(false);
        setRec(rec);
    }
    /**
     * update the target and know if they are hit
     * @param slicers tracking with slicers
     */
    public void update(ArrayList<Slicer> slicers){
        setV(target.getLoc().asVector().sub(getLoc().asVector()).normalised());
        setLoc(getLoc().asVector().add(getV().mul(ShadowDefence.getTS() * getS())).asPoint());
        setRec(getI().getBoundingBoxAt(getLoc()));
        // handle projectile collision with slicer
        if(target.onMap()){
            if(getRec().intersects(getT().getRec())){
                setHit(true);
                target.setHP(getT().getHP() - getD());
                if(target.getHP() <= 0){
                    target.setGone(true);
                    ShadowDefence.getPlayer().setF(ShadowDefence.getPlayer().getF() + target.getR());
                    // when the parent slicer die then split to their children
                    if(target instanceof SuperSlicer){
                        slicers.add(new RegularSlicer(target));
                        slicers.add(new RegularSlicer(target));
                    } else if (target instanceof MegaSlicer){
                        slicers.add(new SuperSlicer(target));
                        slicers.add(new SuperSlicer(target));
                    } else if (target instanceof ApexSlicer){
                        for (int i =0; i < MEGA_SPLIT; i++){ slicers.add(new MegaSlicer(target)); }
                    }
                }
            }
        } else { setHit(true); }
    }
    /**
     * render projectile on the map
     */
    public void render(){ getI().draw(getLoc().x, getLoc().y); }
    /**
     * check if the slicers are hit or not
     * @return a boolean
     */
    public boolean onMap(){ return isNotHit(); }
    /**
     * set up the projectile type
     * @param d damage
     * @param s speed
     */
    public void setType(int d, int s){
        setD(d);
        setS(s);
    }
    /*Getter and setter*/
    public Image getI() { return image; }
    public void setI(Image image){ this.image = image; }
    public Rectangle getRec() { return rec; }
    public void setRec(Rectangle rec) { this.rec = rec; }
    public Point getLoc() { return location; }
    public void setLoc(Point location) { this.location = location; }
    public Slicer getT() { return target; }
    public void setT(Slicer target) { this.target = target; }
    public int getS() { return speed; }
    public void setS(int speed) { this.speed = speed; }
    public Vector2 getV() { return velocity; }
    public void setV(Vector2 velocity) { this.velocity = velocity; }
    public boolean isNotHit() { return !hit; }
    public void setHit(boolean hit) { this.hit = hit; }
    public int getD() { return damage; }
    public void setD(int damage) { this.damage = damage; }
}
