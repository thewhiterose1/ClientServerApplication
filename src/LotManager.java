import datatypes.*;
import net.jini.core.event.RemoteEventListener;
import net.jini.core.transaction.Transaction;
import net.jini.core.transaction.TransactionFactory;
import net.jini.core.transaction.server.TransactionManager;
import net.jini.export.Exporter;
import net.jini.jeri.BasicILFactory;
import net.jini.jeri.BasicJeriExporter;
import net.jini.jeri.tcp.TcpServerEndpoint;
import net.jini.space.JavaSpace05;
import net.jini.space.MatchSet;
import security.SpaceUtils;

import java.util.ArrayList;
import java.util.Collection;

public class LotManager {

    private JavaSpace05 space;
    private TransactionManager mgr;
    private final int COMMIT_TIME = 180 * 1000;
    private final int WAIT_TIME = 5 * 1000;

    private AuctionUI interfaceObj;

    public LotManager(AuctionUI interfaceObj) {
        // Variable initialisation
        this.interfaceObj = interfaceObj;

       // JavaSpace initialisation
        space = (JavaSpace05) SpaceUtils.getSpace();
        if (space == null) {
            System.err.println("Failed to find the javaspace");
            System.exit(1);
        }

        // Transaction manager initialisation
        mgr = SpaceUtils.getManager();
        if (mgr == null) {
            System.err.println("Failed to find the transaction manager");
            System.exit(1);
        }

        // Setup of notify functionality - gives live updates of all relevant objects to interfaces
        try {
            space.notify(new Lot(), null, getEventListener(interfaceObj), COMMIT_TIME, null);
            space.notify(new RefreshLot(), null, getEventListener(interfaceObj), COMMIT_TIME, null);
            space.notify(new BuyNowToken(), null, getEventListener(interfaceObj), COMMIT_TIME, null);
            space.notify(new AcceptHighestBidToken(), null, getEventListener(interfaceObj), COMMIT_TIME, null);
        }
        catch(Exception e) {

        }
    }

    /**
     * Returns all Lot objects from the JavaSpace
     * @return ArrayList of all Lot objects in JavaSpace
     */
    public ArrayList<Lot> getAllLots() {
        // List to be returned representing all Lot objects
        ArrayList<Lot> lotList = new ArrayList<>();
        Lot template = new Lot();
        Collection<Lot> templates = new ArrayList<>();
        templates.add(template);

        try {
            MatchSet results = space.contents(templates, null, WAIT_TIME, 100);

            Lot result = (Lot) results.next();
            while (result != null){
                lotList.add(result);
                result = (Lot)results.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lotList;
    }

    /**
     * Makes a bid on a Lot object
     * @param user User object representing the buyer making the bid
     * @param bidPrice Float value representing the price the buyer is offering
     * @param lot Lot object representing the lot the bid is being placed on
     * @return returns Lot object representing the updated selected Lot
     */
    public Lot makeBid(User user, Float bidPrice, Lot lot) {
        Lot template = lot;
        Bid newBid = new Bid(user, bidPrice);
        // Transaction utilised to ensure Lot object is not lost since it is taken from the space,
        // in order to update it with a new bid
        Transaction.Created trc = null;
        try {
            trc = TransactionFactory.create(mgr, WAIT_TIME);
        } catch (Exception e) {
            System.out.println("Could not create transaction " + e);
        }

        Transaction txn = trc.transaction;

        try {
            Lot got = (Lot) space.take(template, txn, WAIT_TIME);
            got.addBid(newBid);
            space.write(got, txn, COMMIT_TIME);
            txn.commit();
            return got;
        } catch ( Exception e) {
            e.printStackTrace();
            return lot;
        }
    }

    /**
     * Returns all bid objects
     * @param lot object you are returning the bids of
     * @return ArrayList of Bid objects for particular lot
     */
    public ArrayList<Bid> getBids(Lot lot) {
        Lot template = lot;
        try {
            Lot got = (Lot) space.read(template, null, WAIT_TIME);
            if (got != null) {
                return got.bids;
            }
            return new ArrayList<Bid>();

        } catch ( Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Writes new Lot object to the JavaSpace
     * @param seller User object representing the seller of the lot
     * @param lotName String value representing the name of the lot
     * @param lotDesc String value representing the description of the lot
     * @param lotBuyNow Float value representing the seller's buy now price
     */
    public void newLot(User seller, String lotName, String lotDesc, float lotBuyNow) {
        try {
            Lot lot = new Lot(seller, lotName, lotDesc, lotBuyNow);
            space.write(lot, null, COMMIT_TIME);
        } catch ( Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Removes Lot object from the JavaSpace
     * @param lot object user attempting to remove from the JavaSpace
     */
    public void removeLot(Lot lot)  {
        try {
            space.write(new RefreshLot(), null, WAIT_TIME);
            space.take(lot, null, COMMIT_TIME);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Invoked when user clicks 'Buy it Now' button in ViewLotUI
     * @param buyer the User object representing the buyer
     * @param lot the lot object representing the lot being bought
     */
    public void buyItNow(User buyer, Lot lot) {
        removeLot(lot);
        try {
            space.write(new BuyNowToken(buyer, lot), null, WAIT_TIME);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Called when lot is bought now, checks if user owned bought lot
     * @param user User object to check if owner of lot
     * @return Lot object, or null, depending if the user owned the lot
     */
    public Lot buyNowCheck(User user) {
        BuyNowToken template = new BuyNowToken();
        try {
            BuyNowToken got = (BuyNowToken) space.read(template, null, WAIT_TIME);
            if (got.boughtLot.seller.username.equals(user.username)) {
                space.take(got, null, WAIT_TIME);
                return got.boughtLot;
            }
            return null;
        }
        catch (Exception e) {
            return null;
        }
    }

    /**
     * Functionality for accepting the highest bid and removing the lot
     * @param lot lot object that the highest bid is being accepted on
     */
    public void acceptHighestBid(Lot lot) {
        try {
            if (lot.bids.size() > 0) {
                Lot got = (Lot) space.take(lot, null, WAIT_TIME);
                Bid highest = got.bids.get(0);
                for (Bid ele : got.bids) {
                    if (ele.bidPrice < highest.bidPrice) {
                        got.bids.remove(ele);
                        highest = ele;
                    }
                    else {
                        highest = ele;
                    }
                }
                space.write(new AcceptHighestBidToken(lot, highest), null, WAIT_TIME);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Checks if the user of the system had their bid accepted
     * @param user the user of the system
     * @return Returns the token object containing bid and lot information
     */
    public AcceptHighestBidToken checkHighestBid(User user) {
        AcceptHighestBidToken template = new AcceptHighestBidToken();
        try {
            AcceptHighestBidToken got = (AcceptHighestBidToken) space.take(template, null, WAIT_TIME);
            if (got != null) {
                if (user.username.equals(got.highestBid.user.username)) {
                    return got;
                }
            }
            return null;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @param interfaceObj the interface that will be notified of changes to objects
     * @return interface listener for updating object changes
     */
    public RemoteEventListener getEventListener(AuctionUI interfaceObj) {
        // create the exporter
        Exporter myDefaultExporter =
                new BasicJeriExporter(TcpServerEndpoint.getInstance(0),
                        new BasicILFactory(), false, true);

        try {
            // register this as a remote object
            // and get a reference to the 'stub'
            return (RemoteEventListener) myDefaultExporter.export(interfaceObj);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
            return null;
        }
    }
}