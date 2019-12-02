package datatypes;

import net.jini.core.entry.Entry;
import security.SecurityManager;

public class User implements Entry {

    public String username;
    public String password;

    public User() {

    }

    public User(String username, String password) {
        // Check username is valid
        if (SecurityManager.validateUsername(username)) {
            this.username = username;
        }
        else {

        }
        // Check password is valid
        if (SecurityManager.validatePassword(password)) {
            this.password = SecurityManager.hashPassword(password);
        }
    }
}
