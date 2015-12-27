import com.google.gson.JsonArray;
import core.CreateTableQueries;
import core.DBConnection;
import core.EventJsonDownloader;
import core.InsertTableQueries;
import utils.FileManager;


/**
 * Created by ahmetkucuk on 18/11/15.
 */
public class Runner {

    public static final String BASE_PATH = "/Users/ahmetkucuk/Documents/Developer/java/QueryHEK/Result/";

    public static final String AR_JSON_FILE = BASE_PATH + "AR/" + "AR_event_startdate=2012-2-01T00-00-00event_enddate=2012-3-01T23-00-59.json";
    public static final String CH_JSON_FILE = BASE_PATH + "CH/" + "ch_event_startdate=2013-7-01T00-00-00event_enddate=2013-8-01T23-00-59.json";
    public static final String FL_JSON_FILE = BASE_PATH + "FL/" + "FL_event_startdate=2012-01-01T00-00-00event_enddate=2013-12-30T23-00-59.json";
    public static final String SG_JSON_FILE = BASE_PATH + "SG/" + "sg_event_startdate=2012-01-01T00-00-00event_enddate=2012-12-30T23-00-59.json";

    public static final String[] jsonFilesToInsert = new String[]{AR_JSON_FILE, CH_JSON_FILE, FL_JSON_FILE, SG_JSON_FILE};

    public static void main(String[] args) {

//
//        List<Event> eventList = Utilities.convertFileToJSON(JSON_FILE);
//        for(Event e: eventList) {
//            System.out.println(Utilities.generateInsertQueryForAR(e));
//        }

//        new CreateTableQueries().createTables();
//        DBConnection.getInstance().executeFromFile(FileManager.getInstance().getPath("data/spatial_ref_sys.sql"));


//        DBConnection.getInstance().executeFromFile(FileManager.getInstance().getPath("data/NewHekScript.sql"));

//        for(String file : jsonFilesToInser) {
//            InsertTableQueries insertTableQueries = new InsertTableQueries(file);
//            System.out.println(insertTableQueries.getInsertQueries().get(0));
//            insertTableQueries.getInsertGEQueries().stream().forEach(q -> DBConnection.getInstance().executeCommand(q));
//            insertTableQueries.getInsertQueries().stream().forEach(q -> DBConnection.getInstance().executeCommand(q));
//        }

        EventJsonDownloader eventJsonDownloader = new EventJsonDownloader("ch", "2012-12-01T00:00:00", "2012-12-03T23:00:59");
        JsonArray array;
        while((array = eventJsonDownloader.next()) != null) {
            System.out.println(array.size());
        }
    }


}
