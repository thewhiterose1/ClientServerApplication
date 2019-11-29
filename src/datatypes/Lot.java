package datatypes;

import datatypes.Bid;
import datatypes.User;
import net.jini.core.entry.*;
import java.util.ArrayList;

public class Lot implements Entry {

    public User seller;
    public String name, desc;
    public ArrayList<Bid> bids;
    public float buyNowPrice;

    public Lot() {

    }
}
