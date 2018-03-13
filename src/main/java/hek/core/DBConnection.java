package hek.core;

import common.db.DBPrefs;
import hek.utils.Utilities;
import java.sql.*;

/**
 * Created by ahmetkucuk on 22/11/15.
 */
public class DBConnection {

    private Connection connection;

    private DBConnection() {
        try
        {
            System.out.println("Connecting to DB");
            connection = DBPrefs.getDataSource().getConnection();
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
        DBConnection connection = new DBConnection();
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
