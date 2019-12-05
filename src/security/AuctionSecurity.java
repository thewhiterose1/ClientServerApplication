package security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class AuctionSecurity {

    /**
     * Hash function for storing passwords
     * */
    public static String hashPassword(String input) {
        StringBuilder hash = new StringBuilder();

        try {
            MessageDigest sha = MessageDigest.getInstance("SHA-1");
            byte[] hashedBytes = sha.digest(input.getBytes());
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

    public static boolean login(String username, String passwordAttempt) {
        // Hash the attempted password ready for comparison
        passwordAttempt = hashPassword(passwordAttempt);
        return true;
    }

    public static boolean validateUsername(String username) {
        return true;
    }

    public static boolean validatePassword(String password) {
        return true;
    }
}
