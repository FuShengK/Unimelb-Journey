import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;
import java.util.ArrayList;
/**
 * a class for explosive
 */
public class Explosive{
    // the explosive original setting
    private static final int DELAY = 2 * 60;
    private static final int DAMAGE = 500;
    private static final double RADIUS = 200;
    private static final int MEGA_SPLIT = 4;
    private static final int START_DROPPING = 0;
    private static final int BYE = 0;
    private Image image;
    private Rectangle rec;
    private Point location;
    private boolean explosion;
    private int dropping;
    /**
     * start an new explosive
     * @param loc location
     */
    public Explosive(Point loc) {
        setI(new Image("res/images/explosive.png"));
        setLoc(loc);
        setRec(new Rectangle(loc.x,loc.y,RADIUS, RADIUS));
        this.dropping = START_DROPPING;
        this.explosion = false;
    }
    /**
     * Updates the Explosive with slicers.
     */
    public void update(ArrayList<Slicer> slicers) {
        setRec(getI().getBoundingBoxAt(location));
        // if is boom
        if(isNoBoom()){
            image.draw(location.x,location.y);
            // when the delay time of then boom and check with slicers
            if(dropping>=DELAY) {
                // To read from back of the slicers to avoid ConcurrentModificationException
                for (int k = slicers.size() -1;k>=0;k--){
                    Slicer s = slicers.get(k);
                    if (getRec().intersects(s.getRec())) {
                        s.setHP(s.getHP() - DAMAGE);
                        if (s.getHP() <= BYE) {
                            s.setGone(true);
                            slicers.remove(k);
                            // get money when we take down them
                            ShadowDefence.getPlayer().setF(ShadowDefence.getPlayer().getF() + s.getR());
                            // split the slicers when their parent slicer die
                            if (s instanceof SuperSlicer) {
                                slicers.add(new RegularSlicer(s));
                                slicers.add(new RegularSlicer(s));
                            } else if (s instanceof MegaSlicer) {
                                slicers.add(new SuperSlicer(s));
                                slicers.add(new SuperSlicer(s));
                            } else if (s instanceof ApexSlicer) {
                                for (int i = 0; i < MEGA_SPLIT; i++) {
                                    slicers.add(new MegaSlicer(s));
                                }
                            }
                        }
                    }
                } setE(true);
            }
            // keep tracking with time scale
            dropping += ShadowDefence.getTS();
        }
    }
    /*Getter and setter*/
    public Image getI() { return image; }
    public void setI(Image image) { this.image = image; }
    public Rectangle getRec() { return rec; }
    public void setRec(Rectangle rec) { this.rec = rec; }
    public Point getLoc() { return location; }
    public void setLoc(Point location) { this.location = location; }
    public void setE(boolean explosion) { this.explosion = explosion; }
    public boolean isNoBoom() { return !explosion; }
}

