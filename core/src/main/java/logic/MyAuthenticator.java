package logic;

import java.net.Authenticator;
import java.net.PasswordAuthentication;

public class MyAuthenticator extends Authenticator{
    private String user;
    private String passsword;
    @Override
    public PasswordAuthentication getPasswordAuthentication () {
        if (getRequestingScheme().equalsIgnoreCase("basic")) {
            return new PasswordAuthentication (user, passsword.toCharArray());
        }
        return null;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setPasssword(String passsword) {
        this.passsword = passsword;
    }
}