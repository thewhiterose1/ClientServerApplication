import datatypes.Bid;
import datatypes.Lot;
import net.jini.core.event.RemoteEvent;
import net.jini.core.event.UnknownEventException;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
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

    private Lot selectedLot;

    public ViewLotUI(Lot selectedLot) {
        // Variable initialisation
        this.interfacePanel = viewLotScreen;
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
                Lot updatedLot = lotManager.makeBid(AuctionSystem.getAuctionSystem().getUserSession(), bidAmount, getSelectedLot());
                setSelectedLot(updatedLot);
            }
        });

        buyItNowButton.addActionListener(new ActionListener() {
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
     * Listens for changes in bids to the given Lot object and refreshes the bid list accordingly
     * @param remoteEvent
     * @throws UnknownEventException
     * @throws RemoteException
     */
    @Override
    public void notify(RemoteEvent remoteEvent) throws UnknownEventException, RemoteException {
        refreshBids();
    }
}