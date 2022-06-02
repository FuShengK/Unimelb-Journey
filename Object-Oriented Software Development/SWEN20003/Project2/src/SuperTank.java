import bagel.util.Point;
/**
 * a class for super tank
 */
public class SuperTank extends Tower{
    private static final String IMAGE = "res/images/supertank.png";
    private static final int RADIUS = 150;
    private static final int CD = 500;
    private static final int DAMAGE = 3;
    /**
     * set up super tank
     * @param location mouse
     */
    public SuperTank(Point location){
        super(location, IMAGE);
        setTower(RADIUS, CD, DAMAGE);
        setRec(getIM().getBoundingBoxAt(getLoc()));
    }
}
