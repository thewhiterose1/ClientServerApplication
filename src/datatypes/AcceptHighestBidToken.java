package datatypes;

import net.jini.core.entry.Entry;

public class AcceptHighestBidToken implements Entry {

    public Bid highestBid;
    public Lot lot;

    public AcceptHighestBidToken() {

    }

    public AcceptHighestBidToken(Lot lot, Bid highestBid) {
        this.lot = lot;
        this.highestBid = highestBid;
    }
}
