package security;

import javax.swing.*;

public class AuctionSecurity {

    /**
     * Validates username
     * @param username username the user is attempting to enter
     * @return true if valid, false if invalid
     */
    public static boolean validateUsername(String username) {
        String regex = "^[a-zA-Z0-9_]+$";
        if (username.matches(regex)) {
            return true;
        }
        else {
            JOptionPane.showMessageDialog(null, "ERROR: " + username + " - this is an invalid username.");
            return false;
        }
    }

    /**
     * Validates pasword
     * @param password password the user is attempting to enter
     * @return true if valid, false if invalid
     */
    public static boolean validatePassword(String password) {
        String regex = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";
        if (password.matches(regex)) {
            return true;
        }
        else {
            JOptionPane.showMessageDialog(null, "ERROR: " + password + " - this is an invalid password.");
            return false;
        }
    }

    /**
     * Validates text inputs
     * @param text String variable that is to be checked to ensure it conforms with text
     * @return true if valid, false if invalid
     */
    public static boolean validateText(String text) {
        String regex = "^[.,'a-zA-Z0-9 ]+$";
        if (text.matches(regex)) {
            return true;
        }
        else {
            JOptionPane.showMessageDialog(null, "ERROR: " + text + " - this is an invalid value.");
            return false;
        }
    }

    /**
     * Validates currency inputs
     * @param money Validates user has entered some sort of monetary value
     * @return true if valid, false if invalid
     */
    public static boolean validateMoney(String money) {
        try {
            Float.parseFloat(money);
        }
        catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ERROR: " + money + " - this is an invalid currency value.");
            return false;
        }
        return true;
    }
}
