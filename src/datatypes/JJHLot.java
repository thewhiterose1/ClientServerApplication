package datatypes;

import net.jini.core.entry.*;
import java.util.ArrayList;

public class JJHLot implements Entry {

    public JJHUser seller;
    public String name, desc;
    public ArrayList<JJHBid> JJHBids;
    public Float buyNowPrice;

    public JJHLot() {

    }

    public JJHLot(JJHUser seller, String name, String desc, Float buyNowPrice) {
        this.seller = seller;
        this.name = name;
        this.desc = desc;
        this.buyNowPrice = buyNowPrice;
        this.JJHBids = new ArrayList<>();
    }

    /**
     * Adds a new bid to the Lot's bid ArrayList
     * @param newJJHBid Bid object that is being added to the Lot's bids
     */
    public void addBid(JJHBid newJJHBid) {
        JJHBids.add(newJJHBid);
    }

    /**
     * Override method toString, primarily for representing lot objects on the JList for displaying all lots
     * @return string concatenation of Lot information
     */
    public String toString() {
        return "NAME: " + this.name + ". DESC: " + this.desc;
    }
}
