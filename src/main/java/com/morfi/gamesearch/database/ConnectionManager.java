package com.morfi.gamesearch.database;

/**
 * Created by Morfi on 25.01.14.
 */
public class ConnectionManager {
    private DBManager dbConnectionInstance = null;
    private ConnectionDescriptor dbDescriptor = null;

    /**
     * @return the dbDescriptor
     */
    public ConnectionDescriptor getConnectionDescriptor() {
        return dbDescriptor;
    }

    public void setConnectionDescriptor(ConnectionDescriptor connectionDescriptor) {
        dbDescriptor = connectionDescriptor;
    }

    /**
     * @return the dbConnectionInstance
     */
    public DBManager getConnection() {
        return dbConnectionInstance;
    }

    public void setConnection(DBManager connection) {
        dbConnectionInstance = connection;
    }
}