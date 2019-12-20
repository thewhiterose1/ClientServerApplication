package datatypes;

import net.jini.core.entry.Entry;

public class JJHUser implements Entry {

    public String username;
    public String password;

    public JJHUser() {

    }

    public JJHUser(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
