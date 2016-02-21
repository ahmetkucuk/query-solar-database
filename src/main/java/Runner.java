import core.*;
import models.ImageAttributes;
import services.SolarDatabaseAPI;
import utils.ImageListParser;


/**
 * Created by ahmetkucuk on 18/11/15.
 */
public class Runner {

    private static final String START_DATE = "2012-01-01T00:00:00";
    private static final String END_DATE = "2014-12-30T23:59:59";

    public static void main(String[] args) {

        new DBPrefs();
        insertImageFileNamesToDatabase();

    }

    public static void insertImageFileNamesToDatabase (){
        ImageListParser parser = new ImageListParser("/Users/ahmetkucuk/Documents/Developer/virtualmc/Final_Test/allImages.txt");
        ImageAttributes imageAttributes;
        while((imageAttributes = parser.next()) != null) {
            DBConnection.getInstance().executeCommand(imageAttributes.getInsertQuery());
            //System.out.println(imageAttributes.getInsertQuery());
        }
    }

    public static void pullAndInsertDataFromHEK() {

        new DBPrefs();

        GlobalAttributeHolder.init();
        SolarDatabaseAPI api = new SolarDatabaseAPI();
        //api.createDatabaseSchema();
//        api.addAdditionalFunctions();
        api.downloadAndInsertEvents(START_DATE, END_DATE);
        System.out.println("Finished Without Interruption");

    }
}
