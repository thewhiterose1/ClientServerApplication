import security.AuctionSecurity;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginUI extends AuctionUI {
    private JPanel loginScreen;
    private JTextField username;
    private JPasswordField password;
    private JButton login;

    public LoginUI() {
        // Variable initialisation
        this.interfacePanel = loginScreen;

        login.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                login();
            }
        });
    }

    private void login() {
        if (AuctionSecurity.login(username.getText(), password.getText())) {
            AuctionSystem.getAuctionSystem().setUserSession(AuctionSystem.myData.user1);
            AuctionSystem.getAuctionSystem().changePanel("AuctionHub");
        }
        else {
            // Alert informing user of incorrect login details
        }
    }
}
