package datatypes;

import net.jini.core.entry.Entry;

public class Bid implements Entry {

    public Float bidPrice;
    public User user;

    public Bid() {

    }

    public Bid(User buyer, Float bidPrice) {
        this.bidPrice = bidPrice;
        this.user = buyer;
    }

    public String toString() {
        return "User: " + this.user.username + ". Bid Amount: " + this.bidPrice;
    }
}
