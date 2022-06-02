package automail;

import java.util.Map;
import java.util.TreeMap;

// import java.util.UUID;

/**
 * Represents a mail item
 */
public class MailItem {

    /** Represents the destination floor to which the mail is intended to go */
    protected final int destination_floor;
    /** The mail identifier */
    protected final String id;
    /** The time the mail item arrived */
    protected final int arrival_time;
    /** The weight in grams of the mail item */
    protected final int weight;
    /** The service cost of the mail item */
    protected final double service_cost;
    protected final double ACTIVITY_UNITS_INDEPENDENT = 0.1; // a lookup on the servicce fee activity unit
    /** The Activity units price of the mail item */
    protected final double activity_unit_price;
    /** The Markup Percentage of the mail item */
    protected final double markup_percentage;

    /** The robots moving number */
    protected final int MOVING_SCALE = 5; // move up/down costs 5 activity units
    protected int going;

    /**
     * Constructor for a MailItem
     * 
     * @param dest_floor          the destination floor intended for this mail item
     * @param arrival_time        the time that the mail arrived
     * @param weight              the weight of this mail item
     * @param service_cost        the service fee according to the destination floor
     * @param activity_unit_price the unit price for activity
     * @param markup_percentage   the additional percentage that charged in charge
     */
    public MailItem(int dest_floor, int arrival_time, int weight, double service_cost, double activity_unit_price,
            double markup_percentage) {
        this.destination_floor = dest_floor;
        this.id = String.valueOf(hashCode());
        this.arrival_time = arrival_time;
        this.weight = weight;
        this.service_cost = service_cost;
        this.activity_unit_price = activity_unit_price;
        this.markup_percentage = markup_percentage;
    }

    /**
     * To get the movements number
     */
    public void countMovement(int going) {
        this.going = going;
    }

    /**
     * To get the charge information
     */
    public String chargeToString() {
        // To have the charging details
        double activity = MOVING_SCALE * going + ACTIVITY_UNITS_INDEPENDENT;
        double cost = service_cost + activity_unit_price * activity;
        double charge = cost * (1 + markup_percentage);

        return String.format(
                "Mail Item:: ID: %6s | Arrival: %4d | Destination: %2d | Weight: %4d | Charge: %.2f | Cost: %.2f | Fee: %.2f | Activity: %.2f",
                id, arrival_time, destination_floor, weight, charge, cost, service_cost, activity);
    }

    @Override
    public String toString() {
        return String.format("Mail Item:: ID: %6s | Arrival: %4d | Destination: %2d | Weight: %4d", id, arrival_time,
                destination_floor, weight);
    }

    /**
     *
     * @return the activity units of the mail item
     */
    public double getBillableActivity() {
        return (going * MOVING_SCALE + ACTIVITY_UNITS_INDEPENDENT);
    }

    /**
     *
     * @return the activity units cost of the mail item
     */
    public double getBillableActivityCost() {
        return (going * MOVING_SCALE + ACTIVITY_UNITS_INDEPENDENT) * activity_unit_price;
    }

    /**
     *
     * @return the service_cost of the mail item
     */
    public double getServiceCost() {
        return service_cost;
    }

    /**
     *
     * @return the destination floor of the mail item
     */
    public int getDestFloor() {
        return destination_floor;
    }

    /**
     *
     * @return the ID of the mail item
     */
    public String getId() {
        return id;
    }

    /**
     *
     * @return the arrival time of the mail item
     */
    public int getArrivalTime() {
        return arrival_time;
    }

    /**
     *
     * @return the weight of the mail item
     */
    public int getWeight() {
        return weight;
    }

    static private int count = 0;
    static private Map<Integer, Integer> hashMap = new TreeMap<Integer, Integer>();

    @Override
    public int hashCode() {
        Integer hash0 = super.hashCode();
        Integer hash = hashMap.get(hash0);
        if (hash == null) {
            hash = count++;
            hashMap.put(hash0, hash);
        }
        return hash;
    }
}
