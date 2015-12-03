package core;

import utils.Constants;
import utils.Utilities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

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
            String url = "jdbc:postgresql://" + Constants.DB.DB_HOST + "/" + Constants.DB.DB_NAME;

            // get the postgresql database connection
            connection = DriverManager.getConnection(url,Constants.DB.DB_USERNAME, Constants.DB.DB_USER_PASSWORD);

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

    public boolean executeFromFile(String file) {

        String query = Utilities.getFileContent(file);
        return executeCommand(query);


    }
}
