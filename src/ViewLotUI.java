import datatypes.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class ViewLotUI extends AuctionUI {

    private JPanel viewLotScreen;
    private JLabel lotNameLabel;
    private JLabel lotDescLabel;
    private JLabel buyNowPriceLabel;
    private JList bidJList;
    private JButton backButton;
    private JButton buyItNowButton;
    private JButton makeBidButton;

    public Lot selectedLot;

    public ViewLotUI(Lot selectedLot) {
        // Variable initialisation
        this.interfacePanel = viewLotScreen;
        this.lotManager = new LotManager();
        this.selectedLot = selectedLot;
        refreshBids();

        // Lot information initialisation
        lotNameLabel.setText("Name: " + selectedLot.name);
        lotDescLabel.setText("Description: " + selectedLot.desc);
        buyNowPriceLabel.setText("Buy Now Price: " + selectedLot.buyNowPrice);

        // Button functionality
        makeBidButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String input = JOptionPane.showInputDialog(AuctionSystem.getAuctionSystem(),
                        "Enter bid amount", null);
                float bidAmount = Float.parseFloat(input);
                Lot updatedLot = lotManager.makeBid(AuctionSystem.getAuctionSystem().getUserSession(), bidAmount, selectedLot);
                refreshLot(updatedLot);
                refreshBids();
            }
        });

        buyItNowButton.addActionListener(
                new ActionListener() {
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

    /**
     * Updates the selected lot object to ensure it is synchronised its JavaSpace equivalent
     * @param updatedLot updated Lot object to overwrite the selected Lot object
     */
    public void refreshLot(Lot updatedLot) {
        this.selectedLot = updatedLot;
    }

    /**
     * Refreshes the list of Bid objects for a given Lot
     */
    public void refreshBids() {
        // Populate the JList representing active bids for a given lot
        DefaultListModel<Bid> model = new DefaultListModel<>();
        bidJList.setModel(model);
        ArrayList<Bid> bids = lotManager.getBids(selectedLot);
        for (Bid ele : bids) {
            model.addElement(ele);
        }
    }
}