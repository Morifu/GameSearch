package com.morfi.gamesearch.database;

/**
 * Created by Morfi on 25.01.14.
 */
public class ConnectionDescriptor {

    private String connectionName;
    private String hostName;
    private String userName;
    private String defaultDatabase;
    private int portNumber;
    private String password;

    /**
     * @param connectionName
     * @param hostName
     * @param userName
     * @param defaultDatabase
     * @param portNumber
     * @param password
     */
    public ConnectionDescriptor(String connectionName, String hostName,
                                String userName, String defaultDatabase, int portNumber,
                                String password) {
        this.connectionName = connectionName;
        this.hostName = hostName;
        this.userName = userName;
        this.defaultDatabase = defaultDatabase;
        this.portNumber = portNumber;
        this.password = password;
    }

    /**
     * @return the connectionName
     */
    public String getConnectionName() {
        return connectionName;
    }

    /**
     * @param connectionName the connectionName to set
     */
    public void setConnectionName(String connectionName) {
        this.connectionName = connectionName;
    }

    /**
     * @return the hostName
     */
    public String getHostName() {
        return hostName;
    }

    /**
     * @param hostName the hostName to set
     */
    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    /**
     * @return the userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @param userName the userName to set
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * @return the defaultDatabase
     */
    public String getDefaultDatabase() {
        return defaultDatabase;
    }

    /**
     * @param defaultDatabase the defaultDatabase to set
     */
    public void setDefaultDatabase(String defaultDatabase) {
        this.defaultDatabase = defaultDatabase;
    }

    /**
     * @return the portNumber
     */
    public int getPortNumber() {
        return portNumber;
    }

    /**
     * @param portNumber the portNumber to set
     */
    public void setPortNumber(int portNumber) {
        this.portNumber = portNumber;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "ConnectionDescriptor [connectionName=" + connectionName
                + ", hostName=" + hostName + ", userName=" + userName
                + ", defaultDatabase=" + defaultDatabase + ", portNumber="
                + portNumber + "]";
    }

}
