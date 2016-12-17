import core.DBPrefs;
import core.GlobalAttributeHolder;
import org.apache.commons.lang3.time.DateUtils;
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
    private static String END_DATE = "2010-02-01T00:00:00";
    private static final int DAILY_UPDATE_INTERVAL = 1000*60*60*24;
    private static SolarDatabaseAPI api;

    //scp target/query-solar-database-1.0-SNAPSHOT-jar-with-dependencies.jar ahmet@dmlab.cs.gsu.edu:/home/ahmet/workspace/solardb-pull
    //nohup /home/ahmet/tools/jdk1.8.0_73/bin/java -jar query-solar-database-1.0-SNAPSHOT-jar-with-dependencies.jar "/home/ahmet/workspace/solardb-pull/json-files/" "2" >> output_periodic.txt 2>&1
    //mvn clean compile assembly:single

    ///home/ahmet/tools/jdk1.8.0_73/bin/java -jar query-solar-database-1.0-SNAPSHOT-jar-with-dependencies.jar "/home/ahmet/workspace/solardb-pull/json-files/" "1" "2016-07-07T02:00:00" "2016-07-13T02:00:00"
    static String[] arg = new String[] {"/Users/ahmetkucuk/Documents/SolarDB", "1", START_DATE, END_DATE};
    public static void main(String[] args) throws Exception {


        if(args.length < 2) {
            System.out.println(Utilities.getToday2AM());
            System.out.println(Utilities.getYesterday2AM());
            System.err.println("Arguments are missing. Location, Should Continue(0 or 1), Start Date, End Date");
            return;
        }



        new DBPrefs();

        GlobalAttributeHolder.init();
        api = new SolarDatabaseAPI();


        StatusLogger.init(args[0]);
        int page = 1;
        if(args[1].equalsIgnoreCase("0")){
            api.createDatabaseSchema();

            Date qStart = Utilities.getDateFromString(START_DATE);
            Date qEnd;

            Date eDate = Utilities.getDateFromString(END_DATE);
            while(qStart.getTime() < eDate.getTime()){
                qEnd = DateUtils.addMonths(qStart, 1);
                pullAndInsertOldDataFromHEK(Utilities.getStringFromDate(qStart), Utilities.getStringFromDate(qStart), page);
                qStart = qEnd;
            }
            System.out.println("Finished Without Interruption");
        } else if(args[1].equalsIgnoreCase("1")) {
            //Start from previous chunk because last one might not be written completely
            pullAndInsertOldDataFromHEK(args[2], args[3], page);
        } else {
            startWithFixedRate(Utilities.getYesterday2AM());
        }


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
                Date d = DateUtils.addDays(date, 1);
                api.downloadAndInsertEvents(Utilities.getStringFromDate(date), Utilities.getStringFromDate(d), 1);
                System.out.println("Fixed -> Start: " + Utilities.getStringFromDate(date) + " End: " + Utilities.getStringFromDate(d));
                date.setTime(d.getTime());
            }
        }, Utilities.getToday2AM(), DAILY_UPDATE_INTERVAL);

    }

    public static void pullAndInsertOldDataFromHEK(String startDate, String endDate, int page) {

        //
//        api.addAdditionalFunctions();
        System.out.println("Old -> Start: " + startDate + " End: " + endDate);
        api.downloadAndInsertEvents(startDate, endDate, page);


    }
}
