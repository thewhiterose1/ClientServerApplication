import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AuctionSystem {

    // Implementation of singleton design pattern
    public static AuctionSystem app;
    public static JFrame appFrame;

    private JPanel contentDisplay;
    private JButton login;
    private JButton register;

    public static void main (String[] args) {
        app = new AuctionSystem();
    }

    public AuctionSystem() {
        // Initialisation of swing frame
        appFrame = new JFrame("Auction System");
        appFrame.setContentPane(this.contentDisplay);
        appFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        appFrame.pack();
        appFrame.setVisible(true);
        appFrame.setSize(500, 400);


        login.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                login();
            }
        });
        register.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                register();
            }
        });
    }

    public void login() {
        contentDisplay = new LoginUI();
    }

    public void register() {

    }

}
