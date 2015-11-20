import models.Event;
import utils.Constants;
import utils.Utilities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by ahmetkucuk on 18/11/15.
 */
public class Runner {

    public static final String BASE_PATH = "/Users/ahmetkucuk/Documents/Developer/java/QueryHEK/Result/";

    public static final String JSON_FILE = BASE_PATH + "CH/" + "ch_event_startdate=2013-7-01T00-00-00event_enddate=2013-8-01T23-00-59.json";

    public static void main(String[] args) {
        List<Event> eventList = Utilities.convertFileToJSON(JSON_FILE);
        for(Event e: eventList) {
            System.out.println(Utilities.generateInsertQueryForAR(e));
        }

    }

    public static void connectDBTest() {
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
