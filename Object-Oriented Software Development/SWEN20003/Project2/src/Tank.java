import bagel.util.Point;
/**
 * a class for tank
 */
public class Tank extends Tower{
    private static final String IMAGE = "res/images/tank.png";
    private static final int RADIUS = 100;
    private static final int CD = 1000;
    private static final int DAMAGE = 1;
    /**
     * set up Tank
     * @param location mouse
     */
    public Tank(Point location) {
        super(location, IMAGE);
        setTower(RADIUS, CD, DAMAGE);
        setRec(getIM().getBoundingBoxAt(getLoc()));
    }
}
