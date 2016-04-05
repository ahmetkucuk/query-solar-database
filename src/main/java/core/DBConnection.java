package core;

import utils.Utilities;

import java.sql.*;

/**
 * Created by ahmetkucuk on 22/11/15.
 */
public class DBConnection {

    private static final DBConnection instance = new DBConnection();

    private static Connection connection;

    private DBConnection() {
        try
        {
            // the postgresql driver string
            Class.forName("org.postgresql.Driver");

            // the postgresql url
            String url = "jdbc:postgresql://" + DBPrefs.DB_HOST + "/" + DBPrefs.DB_NAME;

            // get the postgresql database connection
            connection = DriverManager.getConnection(url,DBPrefs.DB_USERNAME, DBPrefs.DB_USER_PASSWORD);

        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
            System.exit(1);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            System.exit(2);
        }
    }

    public static DBConnection getInstance() {
        return instance;
    }

    public boolean executeCommand(String query) {
        try {
            return connection.createStatement().execute(query);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(query);
        }
        return false;
    }

    public String getFileName(String q) {
        try {
            return getName(q);
        } catch (SQLException e) {
            e.printStackTrace();
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
