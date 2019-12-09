import datatypes.*;

public class TestData {

    public User user1;
    public User user2;
    public Lot lot1;
    public Lot lot2;


    public TestData() {
        user1 = new User("user1", "password");
        user2 = new User("user2", "password");
        lot1 = new Lot(user1, "Lot 1", "Description for lot 1 item.", 10f);
        lot2 = new Lot(user2, "Lot 2", "Description for lot 2 item.", 15f);
    }
}
