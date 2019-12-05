import datatypes.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class AuctionHubUI {
    private JPanel auctionHub;
    private JPanel hubOptions;
    private JButton newLot;
    private JButton exitButton;
    private JList lotList;

    private ArrayList<Lot> allLots;

    public AuctionHubUI() {
        
    }

    public JPanel getPanel() {
        return auctionHub;
    }
}
