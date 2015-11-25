import core.CreateTableQueries;
import core.DBConnection;
import core.InsertTableQueries;
import utils.FileManager;


/**
 * Created by ahmetkucuk on 18/11/15.
 */
public class Runner {

    public static final String BASE_PATH = "/Users/ahmetkucuk/Documents/Developer/java/QueryHEK/Result/";

    public static final String JSON_FILE = BASE_PATH + "CH/" + "ch_event_startdate=2013-7-01T00-00-00event_enddate=2013-8-01T23-00-59.json";

    public static void main(String[] args) {

//
//        List<Event> eventList = Utilities.convertFileToJSON(JSON_FILE);
//        for(Event e: eventList) {
//            System.out.println(Utilities.generateInsertQueryForAR(e));
//        }

//        new CreateTableQueries().createTables();
//        DBConnection.getInstance().executeFromFile(FileManager.getInstance().getPath("data/spatial_ref_sys.sql"));
        DBConnection.getInstance().executeFromFile(FileManager.getInstance().getPath("data/NewHekScript.sql"));
//        new InsertTableQueries(JSON_FILE).getInsertGEQueries().stream().forEach(q -> System.out.println(q));
        new InsertTableQueries(JSON_FILE).getInsertGEQueries().stream().forEach(q -> DBConnection.getInstance().executeCommand(q));
//        new InsertTableQueries(JSON_FILE).getInsertGEQueries().stream().forEach(q -> DBConnection.getInstance().executeCommand(q));
    }


}
