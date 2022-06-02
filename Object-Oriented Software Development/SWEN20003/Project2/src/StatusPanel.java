import bagel.DrawOptions;
import bagel.Font;
import bagel.util.Colour;
/**
 * A class for status panel
 */
public class StatusPanel extends Panel {
    private static final double PADDING = 4.5;
    private static final int FONT_SIZE = 24;
    private static final int TIME_SCALE_X = 200;
    private static final int STATUS_STRING_X = 450;
    private static final int LIVE_STRING_X = 900;
    /**
     * set up status panel
     */
    public StatusPanel(){ super("res/images/statuspanel.png");}
    /**
     * to render panel on the map
     * @param x x
     * @param y y
     * @param waveNum wave number
     * @param tScale time scale
     * @param status current status
     * @param lives current live
     */
    public void render(double x, double y, int waveNum, int tScale, Status status, int lives){
        // put the panel
        getIM().drawFromTopLeft(x, y);
        // draw the font
        Font font = new Font("res/fonts/DejaVuSans-Bold.ttf", FONT_SIZE);
        // create wave
        font.drawString(String.format("Wave: %d", waveNum), PADDING, ShadowDefence.getHEIGHT() - PADDING);
        // keep tracking the time scale and change the colour
        if (tScale > 1.0) {
            font.drawString(String.format("Time Scale: %d", tScale), TIME_SCALE_X, ShadowDefence.getHEIGHT() - PADDING,
                    new DrawOptions().setBlendColour(Colour.GREEN));
        } else {
            font.drawString(String.format("Time Scale: %d", tScale), TIME_SCALE_X, ShadowDefence.getHEIGHT() -PADDING);
        }
        String statusString = null;
        switch (status){
            case WIN: statusString = "Winner!"; break;
            case PLACING: statusString = "Placing"; break;
            case IN_PROGRESS: statusString = "Wave in Progress"; break;
            case WAITING: statusString = "Awaiting Start"; break;
        }
        font.drawString("Status: " + statusString, STATUS_STRING_X, ShadowDefence.getHEIGHT() - PADDING);
        // draw the lives
        font.drawString(String.format("Lives: %d", lives), LIVE_STRING_X, ShadowDefence.getHEIGHT() - PADDING);
    }
}
