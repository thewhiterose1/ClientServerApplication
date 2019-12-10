import javax.swing.*;

/**
 * Super class for all screens within system
 * Implements functionality common to all interfaces
 */
public class AuctionUI {

    protected JPanel interfacePanel;
    protected AuctionSystem auctionSystem;

    /**
     * Returns the main JPanel container of the current screen
     * @return Main JPanel container of current screen
     */
    public JPanel getPanel() {
        return this.interfacePanel;
    }

    /**
     * Sets the auctionSystem variable for use in interface classes
     */
    protected void setAuctionSystem() {
        this.auctionSystem = AuctionSystem.getAuctionSystem();
    }
}
