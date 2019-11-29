import net.jini.space.JavaSpace;

import javax.swing.*;
import java.awt.*;

public class Getter extends JFrame {
    private static int FIVE_SECOMDS = 1000 * 5;  //1000 miliseconds, multiplied by 5

    private JavaSpace space;
    private JTextField outString;

    public Getter() {
        space = SpaceUtils.getSpace();
        if (space == null){
            System.err.println("Failed to find the javaspace");
            System.exit(1);
        }

        initComponents ();
        this.setSize(300,150);
    }

    private void initComponents() {
        setTitle("JavaSpaces Getter");
        addWindowListener (new java.awt.event.WindowAdapter () {
            public void windowClosing (java.awt.event.WindowEvent evt) {
                exitForm (evt);
            }
        }   );

        Container cp = getContentPane();
        cp.setLayout(new BorderLayout());

        JPanel jPanel1 = new JPanel();
        jPanel1.setLayout (new FlowLayout ());

        JPanel jPanel2 = new JPanel();
        jPanel2.setLayout (new FlowLayout ());

        JLabel textLabel = new JLabel();
        textLabel.setText ("Value retreived ");
        jPanel1.add(textLabel);

        outString = new JTextField(12);
        outString.setText("");
        outString.setEditable(false);
        jPanel1.add(outString);

        cp.add (jPanel1, "North");

        JButton getButton = new JButton();
        getButton.setText(" Get ");
        getButton.addActionListener (new java.awt.event.ActionListener () {
            public void actionPerformed (java.awt.event.ActionEvent evt) {
                getSobj (evt);
            }
        }   );

        jPanel2.add(getButton);
        cp.add (jPanel2, "Center");
    }

    private void exitForm(java.awt.event.WindowEvent evt) {
        System.exit (0);
    }

    private void getSobj (java.awt.event.ActionEvent evt) {
        User template = new User();
        try {
            User got = (User)space.take(template, null, FIVE_SECOMDS);
            if (got == null)
                outString.setText("No object found");
            else
                outString.setText(got.username);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        new Getter().setVisible(true);
    }
}
