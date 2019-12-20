import datatypes.JJHLot;
import datatypes.JJHUser;
import net.jini.core.transaction.TransactionException;
import net.jini.space.JavaSpace;
import security.SpaceUtils;

import java.rmi.RemoteException;
import java.util.ArrayList;

public class TestData {

    public JJHUser JJHUser1;
    public JJHUser JJHUser2;
    public ArrayList<JJHLot> JJHLots = new ArrayList<>();

    private JavaSpace space;
    private int ONE_MINUTE = 180 * 1000;


    public TestData() throws RemoteException, TransactionException {

        // JavaSpace initialisation
        space = SpaceUtils.getSpace();
        if (space == null){
            System.err.println("Failed to find the javaspace");
            System.exit(1);
        }

        // Users
        JJHUser1 = new JJHUser("user1", "password");
        JJHUser2 = new JJHUser("user2", "password");

        // Lots
        JJHLots.add(new JJHLot(JJHUser1, "Lot 1", "Description for lot 1 item.", 10f));
        JJHLots.add(new JJHLot(JJHUser2, "Lot 2", "Description for lot 2 item.", 15f));
        space.write(JJHLots.get(0), null, ONE_MINUTE);
        space.write(JJHLots.get(1), null, ONE_MINUTE);
    }
}
