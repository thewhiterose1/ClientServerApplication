package datatypes;

import net.jini.core.entry.*;
import java.util.ArrayList;

public class Lot implements Entry {

    public User seller;
    public String name, desc;
    public ArrayList<Bid> bids;
    public Float buyNowPrice;

    public Lot() {

    }

    public Lot(User seller, String name, String desc, Float buyNowPrice) {
        this.seller = seller;
        this.name = name;
        this.desc = desc;
        this.buyNowPrice = buyNowPrice;
        this.bids = new ArrayList<>();
    }

    /**
     * Adds a new bid to the Lot's bid ArrayList
     * @param newBid Bid object that is being added to the Lot's bids
     */
    public void addBid(Bid newBid) {
        bids.add(newBid);
    }

    /**
     * Override method toString, primarily for representing lot objects on the JList for displaying all lots
     * @return string concatenation of Lot information
     */
    public String toString() {
        return "NAME: " + this.name + ". DESC: " + this.desc;
    }
}
