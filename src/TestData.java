import datatypes.*;

import java.util.ArrayList;
import java.util.Dictionary;

public class TestData {

    public User user1;
    public User user2;
    public ArrayList<Lot> lots = new ArrayList<>();


    public TestData() {
        // Users
        user1 = new User("user1", "password");
        user2 = new User("user2", "password");

        // Lots
        lots.add(new Lot(user1, "Lot 1", "Description for lot 1 item.", 10f));
        lots.add(new Lot(user2, "Lot 2", "Description for lot 2 item.", 15f));
    }
}
