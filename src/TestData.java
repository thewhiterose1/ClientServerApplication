import datatypes.Lot;
import datatypes.User;
import net.jini.core.transaction.TransactionException;
import net.jini.space.JavaSpace;
import security.SpaceUtils;

import java.rmi.RemoteException;
import java.util.ArrayList;

public class TestData {

    public User user1;
    public User user2;
    public ArrayList<Lot> lots = new ArrayList<>();

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
        user1 = new User("user1", "password");
        user2 = new User("user2", "password");

        // Lots
        lots.add(new Lot(user1, "Lot 1", "Description for lot 1 item.", 10f));
        lots.add(new Lot(user2, "Lot 2", "Description for lot 2 item.", 15f));
        space.write(lots.get(0), null, ONE_MINUTE);
        space.write(lots.get(1), null, ONE_MINUTE);
    }
}
