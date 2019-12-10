import datatypes.*;

import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;

public class AuctionHubUI extends AuctionUI {
    private JPanel auctionHubScreen;
    private JPanel hubOptions;
    private JButton newLotButton;
    private JList lotList;

    private ArrayList<Lot> allLots;

    public AuctionHubUI() {
        // Variable initialisation
        this.interfacePanel = auctionHubScreen;

        // Populate the JList representing active lots within the system
        DefaultListModel<Lot> model = new DefaultListModel<>();
        lotList.setModel(model);
        model.addElement(AuctionSystem.myData.lots.get(0));
        model.addElement(AuctionSystem.myData.lots.get(1));

        // When the user clicks on a given lot, redirect them to the details page for the selected lot
        lotList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                Lot selected = (Lot) lotList.getSelectedValue();
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
    }

    // By utilising notifications, refresh lots list when new one is added by another user
    public void refreshList() {

    }
}
