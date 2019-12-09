package datatypes;

import net.jini.core.entry.*;
import java.util.ArrayList;

public class Lot implements Entry {

    public User seller;
    public String name, desc;
    public ArrayList<Bid> bids;
    public float buyNowPrice;

    public Lot() {

    }

    public Lot(User seller, String name, String desc, float buyNowPrice) {
        this.seller = seller;
        this.name = name;
        this.desc = desc;
        this.buyNowPrice = buyNowPrice;
    }

    public void addBid() {

    }

    // Override method toString, primarily for representing lot objects on the JList for displaying all lots
    public String toString() {
        return this.name + ". " + this.desc;
    }
}
