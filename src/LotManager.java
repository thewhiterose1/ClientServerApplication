import datatypes.*;

import net.jini.core.transaction.Transaction;
import net.jini.core.transaction.TransactionFactory;
import net.jini.core.transaction.server.TransactionManager;
import net.jini.space.JavaSpace;
import net.jini.space.JavaSpace05;
import security.SpaceUtils;

import java.util.ArrayList;
import java.util.Collection;

public class LotManager {

    private JavaSpace05 space;
    private TransactionManager mgr;
    private final int ONE_MINUTE = 60 * 1000;
    private final int FIVE_SECONDS = 5 * 1000;
    private final int MAX_LOTS = 100;

    public LotManager() {
        // JavaSpace initialisation
        space = (JavaSpace05) SpaceUtils.getSpace();
        if (space == null){
            System.err.println("Failed to find the javaspace");
            System.exit(1);
        }

        // Find the transaction manager on the network
        mgr = SpaceUtils.getManager();
        if (mgr == null) {
            System.err.println("Failed to find the transaction manager");
            System.exit(1);
        }
    }

    /**
     * Returns all Lot objects from the JavaSpace
     * @return list of all Lot objects in JavaSpace
     */
    public ArrayList<Lot> getAllLots() {
        // List to be returned representing all Lot objects
        ArrayList<Lot> lotList = new ArrayList<>();
        Lot template = new Lot();
        // Collection of Lot templates to allow returning of multiple Lot objects
        Collection<Lot> templates = new ArrayList<>();
        templates.add(new Lot());

        try {
            // Creation of transaction object
            Transaction.Created trc = null;
            try {
                trc = TransactionFactory.create(mgr, FIVE_SECONDS);
            } catch (Exception e) {
                System.out.println("Could not create transaction " + e);
            }

            Transaction txn = trc.transaction;

            // Reads all Lot objects using a transaction, then aborts the transaction to ensure objects stay in the space
            // this ensures other auction users will be able to view the list of Lot objects in parallel
            while (space.read(template, null, JavaSpace.NO_WAIT) != null) {
                Lot got = (Lot) space.take(template, txn, FIVE_SECONDS);
                lotList.add(got);
            }
            txn.abort();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lotList;
    }

    /**
     * Writes new Lot object to the JavaSpace
     */
    public void newLot(User seller, String lotName, String lotDesc, float lotBuyNow) {
        try {
            Lot lot = new Lot(seller, lotName, lotDesc, lotBuyNow);
            space.write( lot, null, ONE_MINUTE);
        } catch ( Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Removes Lot object from the JavaSpace
     */
    public void removeLot() {

    }
}
