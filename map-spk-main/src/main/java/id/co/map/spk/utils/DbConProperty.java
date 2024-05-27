package id.co.map.spk.utils;

public class DbConProperty {

    private String connectionUrl;
    private String userName;
    private String password;
    private String driverClassName;

    public DbConProperty(String connectionUrl, String userName, String password, String driverClassName) {
        this.connectionUrl = connectionUrl;
        this.userName = userName;
        this.password = password;
        this.driverClassName = driverClassName;
    }

    public String getConnectionUrl() {
        return connectionUrl;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getDriverClassName() {
        return driverClassName;
    }
}
