import datatypes.Lot;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ViewLotUI extends AuctionUI {

    private JPanel viewLotScreen;
    private JLabel lotNameLabel;
    private JLabel lotDescLabel;
    private JLabel buyNowPriceLabel;
    private JList list1;
    private JButton backButton;
    private JButton buyItNowButton;
    private JButton makeBidButton;

    public ViewLotUI(Lot selectedLot) {
        // Variable initialisation
        this.interfacePanel = viewLotScreen;

        // Lot information initialisation
        lotNameLabel.setText("Name: " + selectedLot.name);
        lotDescLabel.setText("Description: " + selectedLot.desc);
        buyNowPriceLabel.setText("Buy Now Price: " + selectedLot.buyNowPrice);

        // Button functionality
        makeBidButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

            }
        });

        buyItNowButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                AuctionSystem.getAuctionSystem().changePanel("AuctionHub");
            }
        });
    }
}