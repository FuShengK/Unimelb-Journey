import bagel.Image;
/**
 * abstrct class for panel
 */
public abstract class Panel {
    private Image image;
    /**
     * set up panel
     * @param IM it is the string name from file
     */
    public Panel(String IM){ this.image = new Image(IM); }
    /*Getter and setter*/
    public Image getIM(){ return image; }
}
