import datatypes.User;
import net.jini.core.transaction.Transaction;
import net.jini.core.transaction.TransactionFactory;
import net.jini.core.transaction.server.TransactionManager;
import net.jini.space.JavaSpace;
import net.jini.space.JavaSpace05;
import security.AuctionSecurity;
import security.SpaceUtils;

import javax.swing.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

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
            MessageDigest sha = MessageDigest.getInstance("SHA-1");
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
    public User login(String username, String password) {
        // Check the credentials are allowed by regex
        if (!(AuctionSecurity.validateUsername(username) && AuctionSecurity.validatePassword(password))) return null;
        // Hash the attempted password ready for comparison
        String hashedPassword = hashPassword(password);
        // If credentials are correct
        for (User ele : getAllUsers()) {
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
    public User register(String username, String password) {
        // Check the credentials are allowed by regex
        if (!(AuctionSecurity.validateUsername(username) && AuctionSecurity.validatePassword(password))) return null;
        // Check username does not already exist
        for (User ele : getAllUsers()) {
            if (ele.username.equals(username)) {
                JOptionPane.showMessageDialog(null, "ERROR: Username already taken.");
                return null;
            }
        }

        // Actually register the new user
        User user = new User(username, hashPassword(password));
        try {
            space.write(user, null, COMMIT_TIME);
        } catch ( Exception e) {
            e.printStackTrace();
            return null;
        }
        return user;
    }

    /**
     * Gets all User objects to be checked when validating credentials
     * @return ArrayList of all User objects
     */
    public ArrayList<User> getAllUsers() {
        ArrayList<User> users = new ArrayList<>();
        User template = new User();

        try {
            // Creation of transaction object
            Transaction.Created trc = null;
            try {
                trc = TransactionFactory.create(mgr, WAIT_TIME);
            } catch (Exception e) {
                System.out.println("Could not create transaction " + e);
            }

            Transaction txn = trc.transaction;

            while (space.read(template, null, JavaSpace.NO_WAIT) != null) {
                User got = (User) space.take(template, txn, WAIT_TIME);
                users.add(got);
            }
            txn.abort();
        }
        catch ( Exception e){
            e.printStackTrace();
        }
        return users;
    }
}
