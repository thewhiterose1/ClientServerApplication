import datatypes.*;

import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;

public class AuctionHubUI {
    private JPanel auctionHub;
    private JPanel hubOptions;
    private JButton newLot;
    private JList lotList;

    private ArrayList<Lot> allLots;

    public AuctionHubUI() {
        // Populate the JList representing active lots within the system
        DefaultListModel<Lot> model = new DefaultListModel<>();
        lotList.setModel(model);
        model.addElement(AuctionSystem.myData.lot1);
        model.addElement(AuctionSystem.myData.lot2);

        // When the user clicks on a given lot, redirect them to the details page for the selected lot
        lotList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                Lot selected = (Lot) lotList.getSelectedValue();
                System.out.println(selected.buyNowPrice);
            }
        });

        // Go to NewLot creation screen
        newLot.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                System.exit(0);
            }
        });
    }

    // By utilising notifications, refresh lots list when new one is added by another user
    public void refreshList() {

    }

    public JPanel getPanel() {
        return auctionHub;
    }
}
