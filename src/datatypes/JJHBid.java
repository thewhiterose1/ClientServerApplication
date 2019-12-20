package datatypes;

import net.jini.core.entry.Entry;

public class JJHBid implements Entry {

    public Float bidPrice;
    public JJHUser JJHUser;

    public JJHBid() {

    }

    public JJHBid(JJHUser buyer, Float bidPrice) {
        this.bidPrice = bidPrice;
        this.JJHUser = buyer;
    }

    public String toString() {
        return "User: " + this.JJHUser.username + ". Bid Amount: " + this.bidPrice;
    }
}
