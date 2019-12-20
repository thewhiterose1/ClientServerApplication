import datatypes.Bid;
import datatypes.Lot;
import net.jini.core.event.RemoteEvent;
import security.AuctionSecurity;

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
    private JButton acceptBidButton;
    private JButton rmvLotButton;
    private JPanel lotOptions;

    private Lot selectedLot;

    public ViewLotUI(Lot selectedLot) {
        // Variable initialisation
        this.interfacePanel = viewLotScreen;
        this.selectedLot = selectedLot;
        refreshBids();
        initialState();

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
                // If user input is valid
                if (AuctionSecurity.validateMoney(input)) {
                    float bidAmount = Float.parseFloat(input);
                    Lot updatedLot = lotManager.makeBid(AuctionSystem.getAuctionSystem().getUserSession(), bidAmount, getSelectedLot());
                    setSelectedLot(updatedLot);
                }
            }
        });

        buyItNowButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                lotManager.buyItNow(AuctionSystem.getAuctionSystem().getUserSession(), getSelectedLot());
                AuctionSystem.getAuctionSystem().changePanel("AuctionHub");
            }
        });

        acceptBidButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                lotManager.acceptHighestBid(getSelectedLot());
                AuctionSystem.getAuctionSystem().changePanel("AuctionHub");
            }
        });

        rmvLotButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                lotManager.removeLot(getSelectedLot());
                AuctionSystem.getAuctionSystem().changePanel("AuctionHub");
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
    public void setSelectedLot(Lot updatedLot) {
        this.selectedLot = updatedLot;
    }

    /**
     * Gets the Lot currently selected, displayed on the ViewLotUI
     * @return Lot object representing the currently selected lot
     */
    public Lot getSelectedLot() { return this.selectedLot; }

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

    /**
     * Initial state of the interface
     * Determines which elements should be displayed if the user owns the lot being viewed or not
     */
    public void initialState() {
        if (this.selectedLot.seller.username.equals(AuctionSystem.getAuctionSystem().getUserSession().username)) {
            buyItNowButton.setVisible(false);
            makeBidButton.setVisible(false);
            rmvLotButton.setVisible(true);
            if (selectedLot.bids.size() > 0) {
                acceptBidButton.setVisible(true);
            }
            else {
                acceptBidButton.setVisible(false);
            }
        }
        else {
            buyItNowButton.setVisible(true);
            makeBidButton.setVisible(true);
            rmvLotButton.setVisible(false);
            acceptBidButton.setVisible(false);
        }
    }

    /**
     * Listens for changes in bids to the given Lot object and refreshes the bid list accordingly
     * @param remoteEvent
     */
    @Override
    public void notify(RemoteEvent remoteEvent) {
        refreshBids();
    }
}