package code.ftp;

/**
 * Created by ixotum on 6/29/15.
 */
public class FtpSettings {
    private String host;
    private String name;
    private String password;

    public void setHost(String host) {
        this.host = host;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getHost() {
        return host;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }
}
