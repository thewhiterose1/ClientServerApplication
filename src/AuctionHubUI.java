import datatypes.AcceptHighestBidToken;
import datatypes.Lot;
import net.jini.core.event.RemoteEvent;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class AuctionHubUI extends AuctionUI {
    private JPanel auctionHubScreen;
    private JPanel hubOptions;
    private JButton newLotButton;
    private JList lotJList;
    private JButton refreshButton;

    private ArrayList<Lot> allLots;

    public AuctionHubUI() {
        // Variable initialisation
        this.interfacePanel = auctionHubScreen;
        refreshList();

        // When the user clicks on a given lot, redirect them to the details page for the selected lot
        lotJList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                Lot selected = (Lot) lotJList.getSelectedValue();
                AuctionSystem.getAuctionSystem().getContentDisplay().add(new ViewLotUI(selected).getPanel(), "ViewSelectedLot");
                AuctionSystem.getAuctionSystem().changePanel("ViewSelectedLot");
            }
        });

        // Go to NewLot creation screen
        newLotButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                AuctionSystem.getAuctionSystem().changePanel("NewLot");
            }
        });

        // Manual refresh button for JList representing Lot objects
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                refreshList();
            }
        });
    }

    /**
     * By utilising notifications, refresh lots list when new one is added by another user
      */
    private void refreshList() {
        // Populate the JList representing active lots within the system
        DefaultListModel<Lot> model = new DefaultListModel<>();
        lotJList.setModel(model);
        allLots = lotManager.getAllLots();
        for (Lot ele : allLots) {
            model.addElement(ele);
        }
    }

    @Override
    public void notify(RemoteEvent remoteEvent) {
        refreshList();
        // Buy it now functionality
        Lot buyItNow = lotManager.buyNowCheck(AuctionSystem.getAuctionSystem().getUserSession());
        if (buyItNow != null) {
            JOptionPane.showMessageDialog(null, "Your item: " + buyItNow.name + " was sold!");
        }
        AcceptHighestBidToken acceptHighestBid = lotManager.checkHighestBid(AuctionSystem.getAuctionSystem().getUserSession());
        if (acceptHighestBid != null) {
            JOptionPane.showMessageDialog(null, "Your bid on item " + acceptHighestBid.lot.name + " was accepted!");
        }
    }
}
