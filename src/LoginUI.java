import datatypes.JJHUser;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginUI extends AuctionUI {
    private JPanel loginScreen;
    private JTextField username;
    private JPasswordField password;
    private JButton loginButton;
    private JButton backButton;

    public LoginUI() {
        // Variable initialisation
        this.interfacePanel = loginScreen;

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                login();
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                AuctionSystem.getAuctionSystem().changePanel("MainScreen");
            }
        });
    }

    private void login() {
        // Query the JavaSpace for the loginButton details
        JJHUser JJHUser = accountManager.login(username.getText(), password.getText());
        if (JJHUser != null) {
            AuctionSystem.getAuctionSystem().setJJHUserSession(JJHUser);
            AuctionSystem.getAuctionSystem().changePanel("AuctionHub");
        }
    }
}
