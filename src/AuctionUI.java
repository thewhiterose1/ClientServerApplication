import net.jini.core.event.RemoteEvent;
import net.jini.core.event.RemoteEventListener;
import net.jini.core.event.UnknownEventException;

import javax.swing.*;
import java.rmi.RemoteException;

/**
 * Super class for all screens within system
 * Implements functionality common to all user interfaces
 */
public class AuctionUI implements RemoteEventListener {

    protected JPanel interfacePanel;
    protected AuctionSystem auctionSystem;
    protected LotManager lotManager;
    protected AccountManager accountManager;

    public AuctionUI() {
        lotManager = new LotManager(this);
        accountManager = new AccountManager(this);
    }

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

    /**
     *
     * @param remoteEvent
     * @throws UnknownEventException
     * @throws RemoteException
     */
    @Override
    public void notify(RemoteEvent remoteEvent) throws UnknownEventException, RemoteException {
    }
}
