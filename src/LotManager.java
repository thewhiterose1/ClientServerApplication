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
            space.notify(new JJHLot(), null, getEventListener(interfaceObj), COMMIT_TIME, null);
            space.notify(new JJHRefreshLotToken(), null, getEventListener(interfaceObj), COMMIT_TIME, null);
            space.notify(new JJHBuyNowToken(), null, getEventListener(interfaceObj), COMMIT_TIME, null);
            space.notify(new JJHAcceptHighestBidToken(), null, getEventListener(interfaceObj), COMMIT_TIME, null);
        }
        catch(Exception e) {

        }
    }

    /**
     * Returns all Lot objects from the JavaSpace
     * @return ArrayList of all Lot objects in JavaSpace
     */
    public ArrayList<JJHLot> getAllLots() {
        // List to be returned representing all Lot objects
        ArrayList<JJHLot> JJHLotList = new ArrayList<>();
        JJHLot template = new JJHLot();
        Collection<JJHLot> templates = new ArrayList<>();
        templates.add(template);

        try {
            MatchSet results = space.contents(templates, null, WAIT_TIME, 100);

            JJHLot result = (JJHLot) results.next();
            while (result != null){
                JJHLotList.add(result);
                result = (JJHLot)results.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return JJHLotList;
    }

    /**
     * Makes a bid on a Lot object
     * @param JJHUser User object representing the buyer making the bid
     * @param bidPrice Float value representing the price the buyer is offering
     * @param JJHLot Lot object representing the lot the bid is being placed on
     * @return returns Lot object representing the updated selected Lot
     */
    public JJHLot makeBid(JJHUser JJHUser, Float bidPrice, JJHLot JJHLot) {
        JJHLot template = JJHLot;
        JJHBid newJJHBid = new JJHBid(JJHUser, bidPrice);
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
            JJHLot got = (JJHLot) space.take(template, txn, WAIT_TIME);
            got.addBid(newJJHBid);
            space.write(got, txn, COMMIT_TIME);
            txn.commit();
            return got;
        } catch ( Exception e) {
            e.printStackTrace();
            return JJHLot;
        }
    }

    /**
     * Returns all bid objects
     * @param JJHLot object you are returning the bids of
     * @return ArrayList of Bid objects for particular lot
     */
    public ArrayList<JJHBid> getBids(JJHLot JJHLot) {
        JJHLot template = JJHLot;
        try {
            JJHLot got = (JJHLot) space.read(template, null, WAIT_TIME);
            if (got != null) {
                return got.JJHBids;
            }
            return new ArrayList<JJHBid>();

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
    public void newLot(JJHUser seller, String lotName, String lotDesc, float lotBuyNow) {
        try {
            JJHLot JJHLot = new JJHLot(seller, lotName, lotDesc, lotBuyNow);
            space.write(JJHLot, null, COMMIT_TIME);
        } catch ( Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Removes Lot object from the JavaSpace
     * @param JJHLot object user attempting to remove from the JavaSpace
     */
    public void removeLot(JJHLot JJHLot)  {
        try {
            space.write(new JJHRefreshLotToken(), null, WAIT_TIME);
            space.take(JJHLot, null, COMMIT_TIME);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Invoked when user clicks 'Buy it Now' button in ViewLotUI
     * @param buyer the User object representing the buyer
     * @param JJHLot the lot object representing the lot being bought
     */
    public void buyItNow(JJHUser buyer, JJHLot JJHLot) {
        removeLot(JJHLot);
        try {
            space.write(new JJHBuyNowToken(buyer, JJHLot), null, WAIT_TIME);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Called when lot is bought now, checks if user owned bought lot
     * @param JJHUser User object to check if owner of lot
     * @return Lot object, or null, depending if the user owned the lot
     */
    public JJHLot buyNowCheck(JJHUser JJHUser) {
        JJHBuyNowToken template = new JJHBuyNowToken();
        try {
            JJHBuyNowToken got = (JJHBuyNowToken) space.read(template, null, WAIT_TIME);
            if (got.boughtJJHLot.seller.username.equals(JJHUser.username)) {
                space.take(got, null, WAIT_TIME);
                return got.boughtJJHLot;
            }
            return null;
        }
        catch (Exception e) {
            return null;
        }
    }

    /**
     * Functionality for accepting the highest bid and removing the lot
     * @param JJHLot lot object that the highest bid is being accepted on
     */
    public void acceptHighestBid(JJHLot JJHLot) {
        try {
            if (JJHLot.JJHBids.size() > 0) {
                JJHLot got = (JJHLot) space.take(JJHLot, null, WAIT_TIME);
                JJHBid highest = got.JJHBids.get(0);
                for (JJHBid ele : got.JJHBids) {
                    if (ele.bidPrice < highest.bidPrice) {
                        got.JJHBids.remove(ele);
                        highest = ele;
                    }
                    else {
                        highest = ele;
                    }
                }
                space.write(new JJHAcceptHighestBidToken(JJHLot, highest), null, WAIT_TIME);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Checks if the user of the system had their bid accepted
     * @param JJHUser the user of the system
     * @return Returns the token object containing bid and lot information
     */
    public JJHAcceptHighestBidToken checkHighestBid(JJHUser JJHUser) {
        JJHAcceptHighestBidToken template = new JJHAcceptHighestBidToken();
        try {
            JJHAcceptHighestBidToken got = (JJHAcceptHighestBidToken) space.take(template, null, WAIT_TIME);
            if (got != null) {
                if (JJHUser.username.equals(got.highestJJHBid.JJHUser.username)) {
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