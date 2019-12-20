import datatypes.JJHAcceptHighestBidToken;
import datatypes.JJHLot;
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

    private ArrayList<JJHLot> allJJHLots;

    public AuctionHubUI() {
        // Variable initialisation
        this.interfacePanel = auctionHubScreen;
        refreshList();

        // When the user clicks on a given lot, redirect them to the details page for the selected lot
        lotJList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                JJHLot selected = (JJHLot) lotJList.getSelectedValue();
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
        DefaultListModel<JJHLot> model = new DefaultListModel<>();
        lotJList.setModel(model);
        allJJHLots = lotManager.getAllLots();
        for (JJHLot ele : allJJHLots) {
            model.addElement(ele);
        }
    }

    @Override
    public void notify(RemoteEvent remoteEvent) {
        refreshList();
        // Buy it now functionality
        JJHLot buyItNow = lotManager.buyNowCheck(AuctionSystem.getAuctionSystem().getJJHUserSession());
        if (buyItNow != null) {
            JOptionPane.showMessageDialog(null, "Your item: " + buyItNow.name + " was sold!");
        }
        JJHAcceptHighestBidToken acceptHighestBid = lotManager.checkHighestBid(AuctionSystem.getAuctionSystem().getJJHUserSession());
        if (acceptHighestBid != null) {
            JOptionPane.showMessageDialog(null, "Your bid on item " + acceptHighestBid.JJHLot.name + " was accepted!");
        }
    }
}
