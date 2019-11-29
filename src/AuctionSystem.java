import javax.swing.*;

public class AuctionSystem {

    // Implementation of singleton design pattern
    public static AuctionSystem app;

    private JPanel contentDisplay;
    private JPanel bgDisplay;

    public static void main (String[] args) {
        app = new AuctionSystem();
    }

    public AuctionSystem() {
        JFrame frame = new JFrame("Auction System");
        frame.setContentPane(this.bgDisplay);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

}
