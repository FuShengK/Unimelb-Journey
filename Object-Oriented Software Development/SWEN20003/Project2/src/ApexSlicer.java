import bagel.util.Point;
import java.util.List;
/**
 * A class for ApexSlicers
 */
public class ApexSlicer extends Slicer{
    private static final String IMAGE = "res/images/apexslicer.png";
    private static final int HP = 25;
    private static final double SPEED = 0.75;
    private static final int PENALTY = 16;
    private static final int REWARD = 150;
    /**
     * new ApexSlicer set up
     * @param polyLine load polyline to run on the map
     */
    public ApexSlicer(List<Point> polyLine) {
        super(polyLine, IMAGE);
        setSlicer(HP,SPEED,PENALTY,REWARD);
        setRec(getIM().getBoundingBoxAt(getLoc()));
    }
}
