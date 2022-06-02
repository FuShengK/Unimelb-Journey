import bagel.Image;
import bagel.util.Point;

public class Slicer {

    /*Initial attributes*/
    private final Itinerary[] road;
    private Point slicerPosition;
    private final Image slicers = new Image("res/images/slicer.png");
    private boolean Down = false;
    private int num = 0;

    /**
     * To make slicer run
     * */
    public Slicer(Itinerary[] road){
        // Constructor
        this.road = road;
        /*Slicers run on the start point*/
        slicerPosition = road[0].getStartP();
    }

    /**
     * Let slicer to run the roads
     */
    public void startGoing(int speed){
        if(!Down){
            /*Let slicers run in valid road*/
            if(!road[num].getRec().intersects(slicerPosition)){
                /*If not then initial them*/
                if(num < (road.length - 2)){
                    num++;
                    slicerPosition = road[num].getStartP();
                } else {
                    Down = true;
                }
            }
        }

        /*The speed rate of axis and the next point to go*/
        double nextXRate = speed * road[num].getxSpeed();
        double nextYRate = speed * road[num].getySpeed();
        slicers.draw(slicerPosition.x, slicerPosition.y, road[num].getOption());
        slicerPosition = new Point(slicerPosition.x + nextXRate, slicerPosition.y + nextYRate);
    }


    /**
     * Methods
    * */
    public boolean isDown(){
        return Down;
    }
}
