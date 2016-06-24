import core.DBPrefs;
import core.GlobalAttributeHolder;
import services.SolarDatabaseAPI;
import utils.StatusLogger;
import utils.Utilities;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by ahmetkucuk on 18/11/15.
 */
public class Runner {

    private static String START_DATE = "2010-01-01T00:00:00";
    private static String END_DATE = "2014-12-30T23:59:59";
    private static final int DAILY_UPDATE_INTERVAL = 1000*60*60*24;
    private static SolarDatabaseAPI api;

    //scp target/query-solar-database-1.0-SNAPSHOT.jar ahmet@dmlab.cs.gsu.edu:/home/ahmet/workspace/solardb-pull
    //nohup /home/ahmet/tools/jdk1.8.0_73/bin/java -jar query-solar-database-1.0-SNAPSHOT-jar-with-dependencies.jar "/home/ahmet/workspace/solardb-pull/json-files/" "1" >> output.txt 2>&1
    //mvn clean compile assembly:single
    static String[] arg = new String[] {"/Users/ahmetkucuk/Documents/SolarDB", "1", START_DATE, END_DATE};
    public static void main(String[] args) throws Exception {

        if(args.length < 2) {
            System.err.println("Arguments are missing. Location, Should Continue(0 or 1), Start Date, End Date");
            return;
        } else if (args[1].equalsIgnoreCase("0") && args.length < 4) {
            System.err.println("Arguments are missing. Location, Should Continue(0 or 1), Start Date, End Date");
            return;
        }

        StatusLogger.init(args[0]);
        int page = 1;
        if(args[1].equalsIgnoreCase("0")){
            START_DATE = args[2];
            END_DATE = args[3];
        } else {
            //Start from previous chunk because last one might not be written completely
            page = StatusLogger.getInstance().getLatestDownloadedPage() - 1;
            if(page < 0) {
                page = 1;
                END_DATE = Utilities.getStringFromDate(Utilities.getToday2AM());
            } else {
                START_DATE = StatusLogger.getInstance().getLatestDownloadedStartTime();
                END_DATE = StatusLogger.getInstance().getLatestDownloadedEndTime();
            }
        }
        System.out.println("Started With: ");
        System.out.println("Start Date: " + START_DATE);
        System.out.println("End Date: " + END_DATE);


        new DBPrefs();

        GlobalAttributeHolder.init();
        api = new SolarDatabaseAPI();
        api.createDatabaseSchema();

        pullAndInsertOldDataFromHEK(START_DATE, END_DATE, page);
        startWithFixedRate(Utilities.getToday2AM());

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
                api.downloadAndInsertEvents(Utilities.getStringFromDate(date), Utilities.getStringFromDate(d), 1);
            }
        }, date, DAILY_UPDATE_INTERVAL);

    }

    public static void pullAndInsertOldDataFromHEK(String startDate, String endDate, int page) {

        //
//        api.addAdditionalFunctions();
        api.downloadAndInsertEvents(startDate, endDate, page);
        System.out.println("Finished Without Interruption");

    }
}
