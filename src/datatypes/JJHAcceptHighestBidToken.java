package datatypes;

import net.jini.core.entry.Entry;

public class JJHAcceptHighestBidToken implements Entry {

    public JJHBid highestJJHBid;
    public JJHLot JJHLot;

    public JJHAcceptHighestBidToken() {

    }

    public JJHAcceptHighestBidToken(JJHLot JJHLot, JJHBid highestJJHBid) {
        this.JJHLot = JJHLot;
        this.highestJJHBid = highestJJHBid;
    }
}
