import bagel.DrawOptions;
import bagel.Font;
import bagel.Image;
import bagel.util.Colour;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
/**
 * A class for buy panel
 */
public class BuyPanel extends Panel{
    private ArrayList<PurchaseItem> tower = new ArrayList<>();
    // Initial set for the position
    private static final int TOP_OFF_SET = 64;
    private static final int RIGHT_OFF_SET = 200;
    private static final int LEFT_OFF_SET = 64;
    private static final int ABOVE = 10;
    private static final int GAP = 120;
    private static final int BELOW = 50;
    private static final int STRING_X = 450;
    private static final int STRING_Y_BASE = 20;
    private static final int STRING_GAP = 15;
    private static final int NUM_KEY_STRING = 4;
    private static final int TOP = 25;
    private static final int KEY_STRING_SET = 14;
    private static final int FOND_SET = 48;
    private static final int TANK = 0;
    private static final int TANK_PRICE = 250;
    private static final int SUPER_TANK = 1;
    private static final int SUPER_TANK_PRICE = 600;
    private static final int PLANE = 2;
    private static final int PLANE_PRICE = 500;
    /**
     * set up buy panel image
     */
    public BuyPanel(){ super("res/images/buypanel.png");}
    /**
     * render all stuff on the panel
     * @param x x
     * @param y y
     * @param fond fond
     */
    public void render(double x, double y, int fond){
        getIM().drawFromTopLeft(x, y);
        // put Tank
        put(LEFT_OFF_SET, getIM().getHeight() / 2 - ABOVE, getT(TANK), fond);
        // put Super Tank
        put(LEFT_OFF_SET + GAP, getIM().getHeight() / 2 - ABOVE, getT(SUPER_TANK), fond);
        // put plane
        put(LEFT_OFF_SET + GAP * 2, getIM().getHeight() / 2 - ABOVE, getT(PLANE), fond);
        // the keys string set
        Font keyBindFont = new Font("res/fonts/DejaVuSans-Bold.ttf", KEY_STRING_SET);
        List<String> str = Arrays.asList("Key binds:", "S - Start Wave",
                "L - Increase Timescale","K - Decrease Timescale");
        int i;
        // draw the key bind on the buy panel
        keyBindFont.drawString(str.get(0), STRING_X, STRING_Y_BASE);
        for(i=1;i<NUM_KEY_STRING;i++){
            keyBindFont.drawString(str.get(i), STRING_X, STRING_Y_BASE + TOP + STRING_GAP*(i-1));
        }
        // draw our fond
        Font font = new Font("res/fonts/DejaVuSans-Bold.ttf", FOND_SET);
        font.drawString(String.format("$%d", fond), ShadowDefence.getWIDTH() - RIGHT_OFF_SET, TOP_OFF_SET);
    }
    /**
     * put information on the panel
     * @param x x
     * @param y y
     * @param item item type
     * @param fond fond
     */
    private void put(double x, double y, PurchaseItem item, int fond) {
        item.getIM().draw(x, y);
        Font font = new Font("res/fonts/DejaVuSans-Bold.ttf", 20);
        String price = String.format("$%d", item.getP());
        double width = font.getWidth(price);

        // Draw the price on
        if(item.getP() <= fond){
            font.drawString(price, x - width / 2, y + BELOW, new DrawOptions().setBlendColour(Colour.GREEN));
        } else {
            font.drawString(price, x - width / 2, y + BELOW, new DrawOptions().setBlendColour(Colour.RED));
        }
    }
    /**
     * set up towers on the panel
     * @param name name of the item
     * @param price price
     */
    public void setT(String name, int price) {
        name = name.toLowerCase();
        String filename = "res/images/"+ name + ".png";
        PurchaseItem newTower = new PurchaseItem(new Image(filename), price, name);
        tower.add(newTower);
    }
    /**
     * collect tower setup in the arraylist
     * @param num the type of item
     * @return the type of item
     */
    public PurchaseItem getT(int num){
        setT("tank", TANK_PRICE);
        setT("supertank", SUPER_TANK_PRICE);
        setT("airsupport", PLANE_PRICE);
        return tower.get(num);
    }
}
