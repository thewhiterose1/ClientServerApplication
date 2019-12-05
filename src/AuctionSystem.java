import javax.swing.*;
import java.awt.CardLayout;

public class AuctionSystem extends JFrame{

    private JPanel contentDisplay;
    private JPanel mainScreen;

    // Implementation of singleton design pattern for reference to main JFrame across application
    private static AuctionSystem app;
    public static void main (String[] args) {
        app = new AuctionSystem();
    }

    public AuctionSystem() {
        // Initialisation of swing frame
        this.setTitle("AuctionSystem");
        this.setContentPane(this.contentDisplay);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setVisible(true);
        this.setSize(500, 400);

        // Utilisation of CardLayout
        contentDisplay.add(mainScreen, "MainScreen");
        contentDisplay.add(new LoginUI().getPanel(), "LoginScreen");
        contentDisplay.add(new AuctionHubUI().getPanel(), "AuctionHub" );

        // Direct the user to main screen upon application startup
        changePanel("LoginScreen");

        // Initial setup

    }

    // Changes the JPanel currently active on the CardLayout
    public void changePanel(String changeTo) {
        ((CardLayout) contentDisplay.getLayout()).show(contentDisplay, changeTo);
    }

    /*
    * Getters
    */

    public JPanel getContentDisplay() {
        return this.contentDisplay;
    }

    public static AuctionSystem getAuctionSystem() {
        return app;
    }
}
