import bagel.Image;
import bagel.util.Point;
import java.util.List;
/**
 * A class for superslicers
 */
public class SuperSlicer extends Slicer{
    private static final String IMAGE = "res/images/superslicer.png";
    private static final int HP = 1;
    private static final double SPEED = 1.5;
    private static final int PENALTY = 2;
    private static final int REWARD = 15;
    /**
     * set up superslicer
     * @param polyLine load polyline to run
     */
    public SuperSlicer(List<Point> polyLine) {
        super(polyLine, IMAGE);
        setSlicer(HP,SPEED,PENALTY,REWARD);
        setRec(getIM().getBoundingBoxAt(getLoc()));
    }
    /**
     * After MegaSlicer die then spawn
     * @param parent MegaSlicer
     */
    SuperSlicer(Slicer parent){
        super(parent);
        setIM(new Image(IMAGE));
        setSlicer(HP,SPEED,PENALTY,REWARD);
        setRec(getIM().getBoundingBoxAt(getLoc()));
    }
}
