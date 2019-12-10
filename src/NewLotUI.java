import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NewLotUI extends AuctionUI {
    private JFormattedTextField formattedTextField1;
    private JTextField textField1;
    private JButton backButton;
    private JButton createLotButton1;
    private JTextField textField2;
    private JPanel newLotScreen;

    public NewLotUI() {
        // Variable initialisation
        this.interfacePanel = newLotScreen;

        // Creating a new lot entry
        createLotButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

            }
        });

        // Returning to the auction hub UI
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                AuctionSystem.getAuctionSystem().changePanel("AuctionHub");
            }
        });
    }
}
