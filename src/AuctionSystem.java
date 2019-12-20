import datatypes.*;
import net.jini.core.transaction.TransactionException;

import javax.swing.*;
import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;

public class AuctionSystem extends JFrame {

    // Local test data initialisation
    public static TestData myData;

    static {
        try {
            myData = new TestData();
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (TransactionException e) {
            e.printStackTrace();
        }
    }

    private JJHUser JJHUserSession;

    private JPanel contentDisplay;
    private JPanel mainScreen;
    private JButton loginButton;
    private JButton registerButton;

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
        contentDisplay.add(new RegisterUI().getPanel(), "RegisterScreen");

        // Direct the user to main screen upon application startup
        changePanel("MainScreen");

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                changePanel("LoginScreen");
            }
        });

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                changePanel("RegisterScreen");
            }
        });
    }

    // Changes the JPanel currently active on the CardLayout
    public void changePanel(String changeTo) { ((CardLayout) contentDisplay.getLayout()).show(contentDisplay, changeTo); }

    /*
    * Setters
    */

    public void setJJHUserSession(JJHUser JJHUser) { this.JJHUserSession = JJHUser; }

    /*
    * Getters
    */

    public JPanel getContentDisplay() { return this.contentDisplay; }

    public JJHUser getJJHUserSession() { return this.JJHUserSession; }

    public static AuctionSystem getAuctionSystem() { return app; }
}
