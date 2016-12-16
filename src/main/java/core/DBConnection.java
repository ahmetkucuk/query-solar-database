package core;

import utils.Utilities;

import java.sql.*;

/**
 * Created by ahmetkucuk on 22/11/15.
 */
public class DBConnection {

    private static final DBConnection instance = new DBConnection();

    private Connection connection;

    private DBConnection() {
        connect();
    }

    private void connect() {
        try
        {
            DriverManager.deregisterDriver(new org.postgresql.Driver());
            // the postgresql driver string

            // the postgresql url
            String url = "jdbc:postgresql://" + DBPrefs.DB_HOST + "/" + DBPrefs.DB_NAME;

            // get the postgresql database connection
            connection = DriverManager.getConnection(url,DBPrefs.DB_USERNAME, DBPrefs.DB_USER_PASSWORD);

        }
        catch (SQLException e)
        {
            e.printStackTrace();
            System.exit(2);
        }
    }

    public void closeConnection() {
        try {
            if(this.connection != null) {
                this.connection.close();
                connection = null;
            }
        } catch (SQLException e) {}
    }

    public static DBConnection getNewConnection() {
        return new DBConnection();
    }


    public Connection getConnection() {
        return connection;
    }

    public boolean executeCommand(String query) {
        try {
            return connection.createStatement().execute(query);
        } catch (SQLException e) {
            System.out.println("[DBConnection-executeCommand] SQL error: " + e.getErrorCode());
        }
        return false;
    }

    public String getFileName(String q) {
        try {
            return getName(q);
        } catch (SQLException e) {
            System.out.println("[DBConnection-getFileName] SQL error: " + e.getErrorCode());
        }
        return null;
    }

    private String getName(String query) throws SQLException {
        Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery(query);
        rs.next();
        String result = rs.getString(1);
        rs.close();
        st.close();
        return result;
    }

    public boolean executeFromFile(String file) {

        String query = Utilities.getFileContent(file);
        return executeCommand(query);

    }
}
