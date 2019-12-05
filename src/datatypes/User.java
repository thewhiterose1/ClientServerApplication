package datatypes;

import net.jini.core.entry.Entry;
import security.AuctionSecurity;

public class User implements Entry {

    public String username;
    public String password;

    public User() {

    }

    public User(String username, String password) {
        // Check username is valid
        if (AuctionSecurity.validateUsername(username)) {
            this.username = username;
        }
        else {

        }
        // Check password is valid
        if (AuctionSecurity.validatePassword(password)) {
            this.password = AuctionSecurity.hashPassword(password);
        }
    }
}
