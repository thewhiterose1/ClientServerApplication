import security.AuctionSecurity;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginUI {
    private JPanel loginScreen;
    private JTextField username;
    private JPasswordField password;
    private JButton login;

    public LoginUI() {

        login.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                login();
            }
        });
    }

    // Returns the loginScreen panel
    public JPanel getPanel() {
        return loginScreen;
    }

    private void login() {
        if (AuctionSecurity.login(username.getText(), password.getText())) {
            AuctionSystem.getAuctionSystem().changePanel("AuctionHub");
        }
        else {
            // Alert informing user of incorrect login details
        }
    }
}
