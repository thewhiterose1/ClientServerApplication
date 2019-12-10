import datatypes.*;
import javax.swing.*;
import java.awt.CardLayout;

public class AuctionSystem extends JFrame {

    // Local test data initialisation
    public static TestData myData = new TestData();
    private User userSession;

    private JPanel contentDisplay;
    private JPanel mainScreen;

    // Implementation of singleton design pattern for reference to main JFrame across application
    private static AuctionSystem app;
    public static void main (String[] args) {
        app = new AuctionSystem();
    }

    public AuctionSystem() {
        // Initialisation of swing frame
        this.setTitle("Auction Application");
        this.setContentPane(this.contentDisplay);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setVisible(true);
        this.setSize(500, 400);

        // Implementation of CardLayout for controlling navigation within the application
        contentDisplay.add(mainScreen, "MainScreen");
        contentDisplay.add(new LoginUI().getPanel(), "LoginScreen");
        contentDisplay.add(new AuctionHubUI().getPanel(), "AuctionHub" );
        contentDisplay.add(new NewLotUI().getPanel(), "NewLot");

        // Direct the user to main screen upon application startup
        changePanel("LoginScreen");

    }

    // Changes the JPanel currently active on the CardLayout
    public void changePanel(String changeTo) {
        ((CardLayout) contentDisplay.getLayout()).show(contentDisplay, changeTo);
    }

    /*
    * Setters
    */

    public void setUserSession(User user) {
        this.userSession = user;
    }

    /*
    * Getters
    */

    public JPanel getContentDisplay() {
        return this.contentDisplay;
    }

    public User getUserSession() { return this.userSession; }

    public static AuctionSystem getAuctionSystem() {
        return app;
    }
}
