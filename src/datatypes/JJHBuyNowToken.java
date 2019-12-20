package datatypes;

import net.jini.core.entry.Entry;

public class JJHBuyNowToken implements Entry {

    public JJHLot boughtJJHLot;
    public JJHUser buyer;

    public JJHBuyNowToken() {
        
    }

    public JJHBuyNowToken(JJHUser buyer, JJHLot boughtJJHLot) {
        this.boughtJJHLot = boughtJJHLot;
        this.buyer = buyer;
    }
}
