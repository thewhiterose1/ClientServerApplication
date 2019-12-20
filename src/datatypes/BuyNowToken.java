package datatypes;

import net.jini.core.entry.Entry;

public class BuyNowToken implements Entry {

    public Lot boughtLot;
    public User buyer;

    public BuyNowToken() {
        
    }

    public BuyNowToken(User buyer, Lot boughtLot) {
        this.boughtLot = boughtLot;
        this.buyer = buyer;
    }
}
