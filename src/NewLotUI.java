import security.AuctionSecurity;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NewLotUI extends AuctionUI {
    private JFormattedTextField nameTextField;
    private JTextField descTextField;
    private JButton backButton;
    private JButton createLotButton;
    private JTextField buyNowTextField;
    private JPanel newLotScreen;

    public NewLotUI() {
        // Variable initialisation
        this.interfacePanel = newLotScreen;

        // Creating a new Lot object for the JavaSpace
        createLotButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                // If user input is valid
                if (AuctionSecurity.validateMoney(buyNowTextField.getText()) &&
                        AuctionSecurity.validateText(nameTextField.getText()) &&
                        AuctionSecurity.validateText(descTextField.getText())) {
                    lotManager.newLot(
                            AuctionSystem.getAuctionSystem().getJJHUserSession(),
                            nameTextField.getText(),
                            descTextField.getText(),
                            Float.parseFloat(buyNowTextField.getText())
                    );
                    AuctionSystem.getAuctionSystem().changePanel("AuctionHub");
                }
            }
        });

        // Returning to the AuctionHubUI
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                AuctionSystem.getAuctionSystem().changePanel("AuctionHub");
            }
        });
    }
}