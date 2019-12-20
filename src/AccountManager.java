import datatypes.JJHUser;
import net.jini.core.transaction.server.TransactionManager;
import net.jini.space.JavaSpace05;
import net.jini.space.MatchSet;
import security.AuctionSecurity;
import security.SpaceUtils;

import javax.swing.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;

public class AccountManager {

    private JavaSpace05 space;
    private TransactionManager mgr;
    private final int COMMIT_TIME = 3600 * 1000;
    private final int WAIT_TIME = 5 * 1000;

    private AuctionUI interfaceObj;

    public AccountManager(AuctionUI interfaceObj) {
        // JavaSpace initialisation
        space = (JavaSpace05) SpaceUtils.getSpace();
        if (space == null) {
            System.err.println("Failed to find the javaspace");
            System.exit(1);
        }

        // Transaction manager initialisation
        mgr = SpaceUtils.getManager();
        if (mgr == null) {
            System.err.println("Failed to find the transaction manager");
            System.exit(1);
        }

        // Variable initialisation
        this.interfaceObj = interfaceObj;
    }

    /**
     * Hash function for password encryption, utilising SHA-1
     * @param password password to be hashed
     * @return hashed password
     */
    public String hashPassword(String password) {
        StringBuilder hash = new StringBuilder();

        try {
            MessageDigest sha = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = sha.digest(password.getBytes());
            char[] digits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                    'a', 'b', 'c', 'd', 'e', 'f' };
            for (int idx = 0; idx < hashedBytes.length;   idx++) {
                byte b = hashedBytes[idx];
                hash.append(digits[(b & 0xf0) >> 4]);
                hash.append(digits[b & 0x0f]);
            }
        } catch (NoSuchAlgorithmException e) {
            // handle error here.
        }

        return hash.toString();
    }

    /**
     * Login to the the system
     * @param username username to login with
     * @param password password to login with
     * @return true if login successful, false if unsuccessful
     */
    public JJHUser login(String username, String password) {
        // Check the credentials are allowed by regex
        if (!(AuctionSecurity.validateUsername(username) && AuctionSecurity.validatePassword(password))) return null;
        // Hash the attempted password ready for comparison
        String hashedPassword = hashPassword(password);
        // If credentials are correct
        for (JJHUser ele : getAllUsers()) {
            if (ele.username.equals(username) && ele.password.equals(hashedPassword)) {
                return ele;
            }
        }
        JOptionPane.showMessageDialog(null, "ERROR: Account credentials do not exist.");
        return null;
    }

    /**
     * Register a new User in the JavaSpace
     * @param username username to be registered
     * @param password password to be registered
     * @return true if registration successful, false if unsuccessful
     */
    public JJHUser register(String username, String password) {
        // Check the credentials are allowed by regex
        if (!(AuctionSecurity.validateUsername(username) && AuctionSecurity.validatePassword(password))) return null;
        // Check username does not already exist
        for (JJHUser ele : getAllUsers()) {
            if (ele.username.equals(username)) {
                JOptionPane.showMessageDialog(null, "ERROR: Username already taken.");
                return null;
            }
        }

        // Actually register the new user
        JJHUser JJHUser = new JJHUser(username, hashPassword(password));
        try {
            space.write(JJHUser, null, COMMIT_TIME);
        } catch ( Exception e) {
            e.printStackTrace();
            return null;
        }
        return JJHUser;
    }

    /**
     * Gets all User objects to be checked when validating credentials
     * @return ArrayList of all User objects
     */
    public ArrayList<JJHUser> getAllUsers() {
        ArrayList<JJHUser> JJHUsers = new ArrayList<>();
        JJHUser template = new JJHUser();
        Collection<JJHUser> templates = new ArrayList<JJHUser>();
        templates.add(template);

        try {
            MatchSet results = space.contents(templates, null, WAIT_TIME, 100);

            JJHUser result = (JJHUser) results.next();
            while (result != null){
                JJHUsers.add(result);
                result = (JJHUser) results.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return JJHUsers;
    }
}
