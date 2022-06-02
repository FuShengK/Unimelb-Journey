import bagel.Image;
import bagel.util.Point;
import java.util.List;
/**
 * A class for megaslicer
 */
public class MegaSlicer extends Slicer {
    private static final String IMAGE = "res/images/megaslicer.png";
    private static final int HP = 2;
    private static final double SPEED = 1.5;
    private static final int PENALTY = 4;
    private static final int REWARD = 10;
    /**
     * set up megalicer
     * @param polyLine load polyline to run
     */
    public MegaSlicer(List<Point> polyLine) {
        super(polyLine, IMAGE);
        setSlicer(HP,SPEED,PENALTY,REWARD);
        setRec(getIM().getBoundingBoxAt(getLoc()));
    }
    /**
     * after ApexSlicer die then spawn
     * @param parent ApexSlicer
     */
    MegaSlicer(Slicer parent){
        super(parent);
        setIM(new Image(IMAGE));
        setSlicer(HP,SPEED,PENALTY,REWARD);
        setRec(getIM().getBoundingBoxAt(getLoc()));
    }
}
