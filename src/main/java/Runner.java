import models.Event;
import utils.Constants;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by ahmetkucuk on 18/11/15.
 */
public class Runner {

    public static void main(String[] args) {
        Event event = new Event();
        Connection connection = null;
        try
        {
            // the postgresql driver string
            Class.forName("org.postgresql.Driver");

            // the postgresql url
            String url = "jdbc:postgresql://" + Constants.DB.DB_HOST + "/" + Constants.DB.DB_NAME;

            // get the postgresql database connection
            connection = DriverManager.getConnection(url,Constants.DB.DB_USERNAME, Constants.DB.DB_USER_PASSWORD);

            ResultSet r = connection.createStatement().executeQuery("SELECT * FROM pg_catalog.pg_tables");
            while(r.next()) {
                System.out.println(r.getString("tablename"));
            }
            // now do whatever you want to do with the connection
            // ...

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
}
