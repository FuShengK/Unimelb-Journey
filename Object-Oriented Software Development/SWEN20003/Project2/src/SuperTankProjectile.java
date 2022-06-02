import bagel.util.Point;
/**
 * a class for super tank projectile
 */
public class SuperTankProjectile extends Projectile{
    private static final String IMAGE = "res/images/supertank_projectile.png";
    private static final int DAMAGE = 3;
    private static final int SPEED = 10;
    /**
     * set up supertank projectile
     * @param location tracking place
     * @param target slicers
     */
    public SuperTankProjectile(Point location, Slicer target) {
        super(location, target, IMAGE);
        setType(DAMAGE,SPEED);
    }
}
