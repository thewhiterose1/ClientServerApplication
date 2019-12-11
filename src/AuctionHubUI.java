import datatypes.*;

import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;

public class AuctionHubUI extends AuctionUI {
    private JPanel auctionHubScreen;
    private JPanel hubOptions;
    private JButton newLotButton;
    private JList lotJList;
    private JButton lotManagerButton;
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
    public void refreshList() {
        // Populate the JList representing active lots within the system
        DefaultListModel<Lot> model = new DefaultListModel<>();
        lotJList.setModel(model);
        allLots = lotManager.getAllLots();
        for (Lot ele : allLots) {
            model.addElement(ele);
        }
    }
}
