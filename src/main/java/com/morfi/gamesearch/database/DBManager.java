package com.morfi.gamesearch.database;

/**
 * Created by Morfi on 25.01.14.
 */

import android.util.Log;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map.Entry;


public class DBManager {

    Connection connection = null;
    String name;
    String url;
    String login;
    String password;
    ConnectionDescriptor descriptor = null;

    /**
     * @param description
     */
    public DBManager(ConnectionDescriptor description) {
        this.descriptor = description;
        setupConnection(description.getHostName(), "",
                description.getUserName(), description.getPassword());
    }

    /**
     * @param host
     * @param dbname
     * @param login
     * @param password
     */
    DBManager(String host, String dbname, String login, String password) {
        setupConnection(host, dbname, login, password);
    }

    private void setupConnection(String host, String dbname, String login,
                                 String password) {
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            name = dbname;
            url = "jdbc:mysql://" + host + "/" + dbname;
            url += "?autoReconnect=true&characterEncoding=UTF-8";
            this.login = login;
            this.password = password;
            System.out.println("URL: " + url);
            System.out.println("Login: " + login);
            System.out.println("Password: "
                    + (password == null ? "" : password));
            connection = DriverManager.getConnection(url, login, password);
            getStatement().executeUpdate("SET NAMES 'utf8'");
            getStatement().executeUpdate("SET CHARACTER SET 'utf8'");
            // System.out.println("Connection: " + connection);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
            connection = null;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private synchronized Statement getStatement() throws SQLException {
        if (connection.isClosed()) {
            connection = DriverManager.getConnection(url, login, password);
        }
        return connection.createStatement();
    }

    private DatabaseMetaData getMetaData() throws SQLException {
        if (connection.isClosed()) {
            connection = DriverManager.getConnection(url, login, password);
        }
        return connection.getMetaData();
    }

    public void printMetaData() {
        try {
            DatabaseMetaData md = getMetaData();
            ResultSet schemas = md.getCatalogs();// md.getTables(null, null,
            // "%", null);
            // //dbManager.execute("select * from test");
            ResultSet schemas2 = md.getSchemas();
            // System.out.println("url is : "+schemas2.getString(1)) ;
            while (schemas2.next()) {
                String tableSchema = schemas2.getString(1); // "TABLE_SCHEM"
                String tableCatalog = schemas2.getString(2); // "TABLE_CATALOG"
                System.out.println("tableSchema is : " + tableSchema);
                System.out.println("tableCatalog is : " + tableCatalog);
            }
            while (schemas.next()) {
                // String tableSchema = schemas.getString(1); // "TABLE_SCHEM"
                String tableCatalog = schemas.getString(1); // "TABLE_CATALOG"
                // System.out.println("tableSchema is : "+tableSchema);
                System.out.println("tableCatalog is : " + tableCatalog);
            }
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
    }

    public void insert(String table, HashMap<String, String> values)
            throws SQLException {
        if (connection == null) {
            return;
        }
        Statement stmt = getStatement();
        String query = "INSERT INTO `";
        query += name;
        query += "`.`";
        query += table;
        query += "` (";

        for (Entry<String, String> entry : values.entrySet()) {
            query += "`";
            query += entry.getKey();
            query += "`, ";
        }
        query = query.substring(0, query.length() - 2);

        query += ") VALUES (";

        for (Entry<String, String> entry : values.entrySet()) {
            query += "'";
            query += entry.getValue();
            query += "', ";
        }
        query = query.substring(0, query.length() - 2);

        query += ");";

        // Server.LOG(query);
        stmt.executeUpdate(query);
    }

    public void update(String table, String idKey, String idValue,
                       HashMap<String, String> values) throws SQLException {

        String condition = "`";
        condition += table;
        condition += "`.`";
        condition += idKey;
        condition += "` = '";
        condition += idValue;
        condition += "'";

        update(table, condition, values);
    }

    public void update(String table, String condition,
                       HashMap<String, String> values) throws SQLException {
        if (connection == null) {
            return;
        }
        Statement stmt = getStatement();
        String query = "UPDATE `";
        query += name;
        query += "`.`";
        query += table;
        query += "` SET ";

        for (Entry<String, String> entry : values.entrySet()) {
            query += "`";
            query += entry.getKey();
            query += "` = '";
            query += entry.getValue();
            query += "', ";
        }
        query = query.substring(0, query.length() - 2);

        query += " WHERE ";
        query += condition;
        query += ";";

        // Server.LOG(query);
        stmt.executeUpdate(query);
    }

    public void delete(String table, String idKey, String idValue)
            throws SQLException {
        String condition = "`";
        condition += table;
        condition += "`.`";
        condition += idKey;
        condition += "` = '";
        condition += idValue;
        condition += "'";

        delete(table, condition);
    }

    public void delete(String table, String condition) throws SQLException {
        if (connection == null) {
            return;
        }
        Statement stmt = getStatement();
        String query = "DELETE FROM `";
        query += name;
        query += "`.`";
        query += table;
        query += "` WHERE ";
        query += condition;
        query += ";";

        // Server.LOG(query);
        stmt.executeUpdate(query);
    }

    public ResultSet selectRow(String table, String idKey, String idValue,
                               LinkedList<String> values) throws SQLException {
        String condition = "`" + table + "`.`" + idKey + "` = '" + idValue
                + "'";
        return select(table, condition, values);
    }

    public ResultSet select(String table, String condition,
                            LinkedList<String> values) throws SQLException {
        if (connection == null) {
            return null;
        }
        Statement stmt = getStatement();

        String query = "SELECT ";

        for (String entry : values) {
            query += entry;
            query += ", ";
        }
        query = query.substring(0, query.length() - 2);

        query += " FROM `";
        query += name;
        query += "`.`";
        query += table;
        query += "` WHERE ";
        query += condition;
        query += ";";
        // Server.LOG(query);

        return stmt.executeQuery(query);
    }

    public ResultSet execute(String query) throws SQLException {
        if (connection == null) {
            return null;
        }
        Statement stmt = getStatement();
        // Server.LOG(query);
        return stmt.executeQuery(query);
    }

    public void update(String query) throws SQLException {
        if (connection == null) {
            return;
        }
        Statement stmt = getStatement();
        // Server.LOG(query);
        stmt.executeUpdate(query);
    }

    public String generateList(LinkedList<String> values) {
        String query = new String();
        for (String entry : values) {
            query += entry;
            query += ", ";
        }
        return query.substring(0, query.length() - 2);
    }

    /**
     * @return the descriptor
     */
    public ConnectionDescriptor getDescriptor() {
        return descriptor;
    }

    /**
     * @param descriptor the descriptor to set
     */
    public void setDescriptor(ConnectionDescriptor descriptor) {
        this.descriptor = descriptor;
    }

    public void close() {
        try {
            if (!connection.isClosed()) {
                connection.close();
                Log.v("DBManager", "Closing DB Connection!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
