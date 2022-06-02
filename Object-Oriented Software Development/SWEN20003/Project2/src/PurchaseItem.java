import bagel.Image;
/**
 * class for purchasing item
 */
public class PurchaseItem {
    private Image image;
    private int price;
    private String name;
    /**
     * set up Purchase items
     * @param theIM the item image
     * @param thePrice the price
     * @param theName the name of item
     */
    public PurchaseItem(Image theIM, int thePrice, String theName) {
        setIM(theIM);
        setP(thePrice);
        setN(theName);
    }
    /*Getter and setter*/
    public Image getIM() { return image; }
    public void setIM(Image image) { this.image = image; }
    public int getP() { return price; }
    public void setP(int price) { this.price = price; }
    public void setN(String name) { this.name = name; }
}
