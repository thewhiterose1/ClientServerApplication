import datatypes.Bid;
import datatypes.Lot;
import datatypes.RefreshLot;
import datatypes.User;
import net.jini.core.event.RemoteEventListener;
import net.jini.core.transaction.Transaction;
import net.jini.core.transaction.TransactionFactory;
import net.jini.core.transaction.server.TransactionManager;
import net.jini.export.Exporter;
import net.jini.jeri.BasicILFactory;
import net.jini.jeri.BasicJeriExporter;
import net.jini.jeri.tcp.TcpServerEndpoint;
import net.jini.space.JavaSpace;
import net.jini.space.JavaSpace05;
import security.SpaceUtils;

import java.util.ArrayList;

public class LotManager {

    private JavaSpace05 space;
    private TransactionManager mgr;
    private final int COMMIT_TIME = 1800 * 1000;
    private final int WAIT_TIME = 5 * 1000;

    private AuctionUI interfaceObj;

    public LotManager(AuctionUI interfaceObj) {
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

        // Variable initialisation
        this.interfaceObj = interfaceObj;
        // Setup of notify functionality - gives live updates of all relevant objects to interfaces
        try {
            space.notify(new Lot(), null, getEventListener(interfaceObj), COMMIT_TIME, null);
            space.notify(new RefreshLot(), null, getEventListener(interfaceObj), WAIT_TIME, null);
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

        try {
            // Creation of transaction object
            Transaction.Created trc = null;
            try {
                trc = TransactionFactory.create(mgr, WAIT_TIME);
            } catch (Exception e) {
                System.out.println("Could not create transaction " + e);
            }

            Transaction txn = trc.transaction;

            // Reads all Lot objects using a transaction, then aborts the transaction to ensure objects stay in the space
            // this ensures other auction users will be able to view the list of Lot objects in parallel
            while (space.read(template, null, JavaSpace.NO_WAIT) != null) {
                Lot got = (Lot) space.take(template, txn, WAIT_TIME);
                lotList.add(got);
            }
            txn.abort();
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
            space.write(got, null, COMMIT_TIME);
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
            return got.bids;
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
            space.take(lot, null, COMMIT_TIME);
            space.write(new RefreshLot(), null, WAIT_TIME);
        } catch (Exception e) {
            e.printStackTrace();
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
