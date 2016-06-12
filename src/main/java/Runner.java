import core.DBPrefs;
import core.GlobalAttributeHolder;
import services.SolarDatabaseAPI;
import utils.Utilities;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by ahmetkucuk on 18/11/15.
 */
public class Runner {

    private static final String START_DATE = "2010-01-01T00:00:00";
    private static final String END_DATE = "2014-12-30T23:59:59";
    private static final int DAILY_UPDATE_INTERVAL = 1000*60*60*24;
    private static SolarDatabaseAPI api;

    public static void main(String[] args) throws Exception {


        new DBPrefs();

        GlobalAttributeHolder.init();
        api = new SolarDatabaseAPI();
        api.createDatabaseSchema();

        Date date = Utilities.getToday2AM();
        pullAndInsertOldDataFromHEK(date);
        startWithFixedRate(date);

//        new DBPrefs();
//        System.out.println(ImageAttributes.getCreateTableQuery(SECONDARY_TABLENAME));
//        System.out.println(ImageAttributes.getCreateTableQuery(PRIMARY_TABLENAME));
//        retrieveClosestImageNames();
        //insertImageFileNamesToDatabase("/Users/ahmetkucuk/Documents/Developer/virtualmc/Final_Test/primary.txt", PRIMARY_TABLENAME);

    }
    public static void startWithFixedRate(Date date) {

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Date d = new Date(date.getTime());
                d.setTime(date.getTime() + DAILY_UPDATE_INTERVAL);
                api.downloadAndInsertEvents(Utilities.getStringFromDate(date), Utilities.getStringFromDate(d));
            }
        }, date, DAILY_UPDATE_INTERVAL);

    }

    public static void pullAndInsertOldDataFromHEK(Date until) {

        //
//        api.addAdditionalFunctions();
        api.downloadAndInsertEvents(START_DATE, Utilities.getStringFromDate(until));
        System.out.println("Finished Without Interruption");

    }
}
