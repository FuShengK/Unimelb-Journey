import bagel.Image;
import bagel.util.Point;
import java.util.List;
/**
 * a class for regularslicer
 */
public class RegularSlicer extends Slicer {
    private static final String IMAGE = "res/images/slicer.png";
    private static final int HP = 1;
    private static final double SPEED = 2.0;
    private static final int PENALTY = 1;
    private static final int REWARD = 2;
    /**
     * set up regularslicer
     * @param polyLine load polyline to run
     */
    public RegularSlicer(List<Point> polyLine) {
        super(polyLine, IMAGE);
        setSlicer(HP,SPEED,PENALTY,REWARD);
        setRec(getIM().getBoundingBoxAt(getLoc()));
    }
    /**
     * when SuperSlicer die then spawn RegularSlicer
     * @param parent SuperSlicer
     */
    RegularSlicer(Slicer parent){
        super(parent);
        setIM(new Image(IMAGE));
        setSlicer(HP,SPEED,PENALTY,REWARD);
        setRec(getIM().getBoundingBoxAt(getLoc()));
    }
}
