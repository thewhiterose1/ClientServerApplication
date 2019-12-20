import datatypes.JJHUser;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RegisterUI extends AuctionUI{
    private JPanel registerScreen;
    private JTextField username;
    private JButton backButton;
    private JButton registerButton;
    private JPasswordField password;

    public RegisterUI() {
        // Variable initialisation
        this.interfacePanel = registerScreen;

        // Registration functionality
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                register();
            }
        });

        // Back to main screen
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                AuctionSystem.getAuctionSystem().changePanel("MainScreen");
            }
        });
    }

    public void register() {
        // Register account then log the user in
        JJHUser JJHUser = accountManager.register(username.getText(), password.getText());
        if (JJHUser != null) {
            AuctionSystem.getAuctionSystem().setJJHUserSession(JJHUser);
            AuctionSystem.getAuctionSystem().changePanel("AuctionHub");
        }
    }
}
