import datatypes.JJHBid;
import datatypes.JJHLot;
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

    private JJHLot selectedJJHLot;

    public ViewLotUI(JJHLot selectedJJHLot) {
        // Variable initialisation
        this.interfacePanel = viewLotScreen;
        this.selectedJJHLot = selectedJJHLot;
        refreshBids();
        initialState();

        // Lot information initialisation
        lotNameLabel.setText("Name: " + selectedJJHLot.name);
        lotDescLabel.setText("Description: " + selectedJJHLot.desc);
        buyNowPriceLabel.setText("Buy Now Price: " + selectedJJHLot.buyNowPrice);

        // Button functionality
        makeBidButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String input = JOptionPane.showInputDialog(AuctionSystem.getAuctionSystem(),
                        "Enter bid amount", null);
                // If user input is valid
                if (AuctionSecurity.validateMoney(input)) {
                    float bidAmount = Float.parseFloat(input);
                    JJHLot updatedJJHLot = lotManager.makeBid(AuctionSystem.getAuctionSystem().getJJHUserSession(), bidAmount, getSelectedJJHLot());
                    setSelectedJJHLot(updatedJJHLot);
                }
            }
        });

        buyItNowButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                lotManager.buyItNow(AuctionSystem.getAuctionSystem().getJJHUserSession(), getSelectedJJHLot());
                AuctionSystem.getAuctionSystem().changePanel("AuctionHub");
            }
        });

        acceptBidButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                lotManager.acceptHighestBid(getSelectedJJHLot());
                AuctionSystem.getAuctionSystem().changePanel("AuctionHub");
            }
        });

        rmvLotButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                lotManager.removeLot(getSelectedJJHLot());
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
     * @param updatedJJHLot updated Lot object to overwrite the selected Lot object
     */
    public void setSelectedJJHLot(JJHLot updatedJJHLot) {
        this.selectedJJHLot = updatedJJHLot;
    }

    /**
     * Gets the Lot currently selected, displayed on the ViewLotUI
     * @return Lot object representing the currently selected lot
     */
    public JJHLot getSelectedJJHLot() { return this.selectedJJHLot; }

    /**
     * Refreshes the list of Bid objects for a given Lot
     */
    public void refreshBids() {
        // Populate the JList representing active bids for a given lot
        DefaultListModel<JJHBid> model = new DefaultListModel<>();
        bidJList.setModel(model);
        ArrayList<JJHBid> JJHBids = lotManager.getBids(selectedJJHLot);
        for (JJHBid ele : JJHBids) {
            model.addElement(ele);
        }
    }

    /**
     * Initial state of the interface
     * Determines which elements should be displayed if the user owns the lot being viewed or not
     */
    public void initialState() {
        if (this.selectedJJHLot.seller.username.equals(AuctionSystem.getAuctionSystem().getJJHUserSession().username)) {
            buyItNowButton.setVisible(false);
            makeBidButton.setVisible(false);
            rmvLotButton.setVisible(true);
            if (selectedJJHLot.JJHBids.size() > 0) {
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