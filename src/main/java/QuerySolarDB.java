import core.DBPrefs;
import core.EventAttributeManager;
import edu.gsu.dmlab.isd.monitor.JobStatus;
import edu.gsu.dmlab.isd.monitor.PostgresJobRecordConnection;
import edu.gsu.dmlab.isd.mq.HEKPullerTask;
import edu.gsu.dmlab.isd.mq.TaskConsumer;
import org.apache.commons.lang3.time.DateUtils;
import services.SolarDatabaseAPI;
import utils.StatusLogger;
import utils.Utilities;

import java.text.ParseException;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import edu.gsu.dmlab.isd.mq.TaskQueue;


/**
 * Created by ahmetkucuk on 18/11/15.
 */
public class QuerySolarDB {

    private static final int DAILY_UPDATE_INTERVAL = 1000*60*60*24;
    final static TaskQueue queue =  new TaskQueue("abc");
    final static PostgresJobRecordConnection monitorConnection = new PostgresJobRecordConnection(DBPrefs.getDataSource());

    //scp target/query-solar-database-1.0-SNAPSHOT-jar-with-dependencies.jar ahmet@dmlab.cs.gsu.edu:/home/ahmet/workspace/solardb-pull
    //nohup /home/ahmet/tools/jdk1.8.0_73/bin/java -jar query-solar-database-1.0-SNAPSHOT-jar-with-dependencies.jar "/home/ahmet/workspace/solardb-pull/json-files/" "month" "2016-07-07T02:00:00" "2016-07-13T02:00:00" > output_flares.txt 2>&1
    //mvn clean compile assembly:single

    //nohup /home/ahmet/tools/jdk1.8.0_73/bin/java -jar query-solar-database/target/query-solar-database-1.0-SNAPSHOT-jar-with-dependencies.jar "/home/ahmet/workspace/solardb-pull/json-files/" "month" "2010-07-07T02:00:00" "2017-04-30T02:00:00" "no" > output_all.txt 2>&1 &
    static String[] arg = new String[] {"/Users/ahmetkucuk/Documents", "all", "2016-07-10T02:00:00", "2016-07-13T02:00:00", "createSchema"};
    public static void main(String[] args) throws Exception {
        System.out.println("HEK PULLER STARTED!");

        monitorConnection.createJobRecordTable();
        // Define a TaskConsumer to receive tasks
        TaskConsumer<HEKPullerTask> consumer = new TaskConsumer<HEKPullerTask>(
                queue.getChannel(), HEKPullerTask.class) {
            public void handleTask(HEKPullerTask task) {
                //Task Arrived, change job status: Processing
                monitorConnection.setJobStatus(task.getJobID(), JobStatus.PROCESSING, null);

                // Pull Events in here
                System.out.println("Handling Task: " + task);

                //Task completed, change job status: Completed
                // If tasks fail change status to failed and add exception
                monitorConnection.setJobStatus(task.getJobID(), JobStatus.COMPLETED, null);
            }
        };
        queue.registerConsumer(consumer);
    }

    public static void startWithFixedRate(final SolarDatabaseAPI api) {

        Timer timer = new Timer();
        Date date = Utilities.getYesterday2AM();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Date d = DateUtils.addDays(date, 1);
                api.pullEvents(Utilities.getStringFromDate(date), Utilities.getStringFromDate(d), 1);
                System.out.println("Fixed -> Start: " + Utilities.getStringFromDate(date) + " End: " + Utilities.getStringFromDate(d));
                date.setTime(d.getTime());
            }
        }, Utilities.getToday2AM(), DAILY_UPDATE_INTERVAL);
    }

    public static void retrieveInChunks(SolarDatabaseAPI api, String startDate, String endDate, int page) throws ParseException {

        Date qStart = Utilities.getDateFromString(startDate);
        Date qEnd;

        Date eDate = Utilities.getDateFromString(endDate);
        while(qStart.getTime() < eDate.getTime()){
            qEnd = DateUtils.addMonths(qStart, 1);
            retrieveAll(api, Utilities.getStringFromDate(qStart), Utilities.getStringFromDate(qEnd), page);
            qStart = qEnd;
        }
    }

    public static void retrieveAll(SolarDatabaseAPI api, String startDate, String endDate, int page) {

        System.out.println("Old -> Start: " + startDate + " End: " + endDate);
        api.pullEvents(startDate, endDate, page);

    }

    public static void oldMain(String[] args) {
        if(args.length < 3) {
            System.out.println(Utilities.getToday2AM());
            System.out.println(Utilities.getYesterday2AM());
            System.err.println("Arguments are missing. Location, Should Continue(0 or 1), Start Date, End Date");
            return;
        }

        String outputDirectory = args[0];
        String mode = args[1];
        String startDate = args[2];
        String endDate = args[3];
        boolean shouldCreateSchema = args[4].equalsIgnoreCase("createSchema");

        System.out.println("Loading global attributes from resource files.");
        EventAttributeManager.init();
        System.out.println("Finished loading.");

        SolarDatabaseAPI api = new SolarDatabaseAPI();
        System.out.println("API initialized.");


        StatusLogger.init(outputDirectory);
        System.out.println("Path is set to StatusLogger: " + outputDirectory);
        int page = 1;

        if(shouldCreateSchema) {
            System.out.println("Schema Creation Started.");
            api.createDatabaseSchema();
            System.out.println("Schema Creation Finished.");
        }

        if(mode.equalsIgnoreCase("month")) {
            System.out.println("Started in Mode: Month");
//            retrieveInChunks(api, startDate, endDate, page);
            System.out.println("Finished Without Interruption");
        } else if(mode.equalsIgnoreCase("all")) {
            System.out.println("Started in Mode: all");
            //Start from previous chunk because last one might not be written completely
            retrieveAll(api, args[2], args[3], page);
        } else {
            System.out.println("Started in Mode: Fixed");
            startWithFixedRate(api);
        }
    }
}
