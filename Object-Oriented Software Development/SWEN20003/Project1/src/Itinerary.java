import bagel.DrawOptions;
import bagel.util.Point;
import bagel.util.Rectangle;

public class Itinerary {

    /*Initial attributes*/
    private final Point startP;
    private final Point endP;
    private final DrawOptions option = new DrawOptions();
    private final double xSpeed;
    private final double ySpeed;
    private final Rectangle rec;

    /**
     * To load the map
     * */
    public Itinerary(Point startP, Point endP) {
        // Constructor
        this.startP = startP;
        this.endP = endP;
        double xLen = endP.x - startP.x;
        double yLen = endP.y - startP.y;

        /*To make slicer run in the road which is valid*/
        double recX = Math.min(startP.x, endP.x);
        double recY = Math.min(startP.y, endP.y);
        rec = new Rectangle(recX, recY, Math.abs(xLen), Math.abs(yLen));

        /*
         * According to the map showing at the beginning, I have made
         * the slicers changing the direction(dt) based on the length of X,Y
         */
        double dt = Math.atan(yLen / xLen);
        if (xLen >= 0) {
            option.setRotation(dt);
        } else if (yLen >= 0) {
            option.setRotation(dt + Math.PI);
        } else {
            option.setRotation(dt - Math.PI);
        }

        /*
         * If I want it to run and let speed at correct axis in the map
         * then I should make sure the value of xLen as well as the yLen, and
         * check it to not get error when one of them of both of them are
         * zero
         */
        double xRate = xLen / Math.abs(xLen);
        double yRate = yLen / Math.abs(yLen);
        double cosDt = Math.cos(Math.abs(dt));
        double sinDt = Math.sin(Math.abs(dt));
        if (xLen != 0 && yLen != 0) {
            xSpeed = cosDt * xRate;
            ySpeed = sinDt * yRate;
        } else if (xLen != 0) {
            xSpeed = cosDt * xRate;
            ySpeed = sinDt;
        } else if(yLen != 0) {
            xSpeed = cosDt;
            ySpeed = sinDt * yRate;
        } else {
            xSpeed = cosDt;
            ySpeed = sinDt;
        }
    }

    /**
     * Methodsll to get the value of each Initial attributes
     */
    public Point getStartP(){return startP;}
    public DrawOptions getOption(){return option;}
    public double getxSpeed(){return xSpeed;}
    public double getySpeed(){return ySpeed;}
    public Rectangle getRec(){return rec;}
}
