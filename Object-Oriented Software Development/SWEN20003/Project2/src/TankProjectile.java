import bagel.util.Point;
/**
 *  a class for tank projectile
 */
public class TankProjectile extends Projectile{
    private static final String IMAGE = "res/images/tank_projectile.png";
    private static final int DAMAGE = 1;
    private static final int SPEED = 10;
    /**
     * set up tank projectile
     * @param location tracking place
     * @param target slicers
     */
    public TankProjectile(Point location, Slicer target) {
        super(location, target, IMAGE);
        setType(DAMAGE,SPEED);
    }
}
