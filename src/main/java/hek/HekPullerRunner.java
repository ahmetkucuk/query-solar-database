package hek;

import common.db.DBPrefs;
import common.db.JobRecordConnectionProvider;
import edu.gsu.dmlab.isd.monitor.JobStatus;
import edu.gsu.dmlab.isd.mq.HEKPullerTask;
import edu.gsu.dmlab.isd.mq.TaskHandler;
import edu.gsu.dmlab.isd.mq.TaskQueue;
import hek.core.EventAttributeManager;
import hek.services.SolarDatabaseAPI;
import hek.utils.StatusLogger;
import hek.utils.Utilities;
import task.core.TaskManager;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by ahmetkucuk on 3/10/18.
 */
public class HekPullerRunner {

    final static String RABBIT_MQ_HOST = "rabbitmq";
    final static int RABBIT_MQ_PORT = 5672;
    final static String RABBIT_MQ_QUEUE_NAME = "HEK_PULLER";
    final static TaskQueue hekPullerQueue =  new TaskQueue(RABBIT_MQ_QUEUE_NAME, RABBIT_MQ_HOST, RABBIT_MQ_PORT);

    final static String LOGGER_PATH = "/isd_log/status_logger/";

    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.println("Hek Puller Runner Started!");

        DBPrefs.waitDefaultDBConnections();
        Files.createDirectories(Paths.get(LOGGER_PATH));

        StatusLogger.init(LOGGER_PATH);
        EventAttributeManager.init();
        SolarDatabaseAPI api = new SolarDatabaseAPI();
        api.createDatabaseSchema();

        TaskHandler<HEKPullerTask> handler = new TaskHandler<HEKPullerTask>(HEKPullerTask.class) {
            public void handleTask(HEKPullerTask task) {
                //Task Arrived, change job status: Processing
                JobRecordConnectionProvider.connection().setJobStatus(task.getJobID(), JobStatus.PROCESSING, null);

                // Pull Events in here
                System.out.println("Handling Task: " + task);

                api.pullEvents(Utilities.getStringFromDate(task.startTime.toDate()),
                        Utilities.getStringFromDate(task.endTime.toDate()), 0);

                //Task completed, change job status: Completed
                // If tasks fail change status to failed and add exception
                JobRecordConnectionProvider.connection().setJobStatus(task.getJobID(), JobStatus.COMPLETED, null);
            }
        };
        hekPullerQueue.registerConsumer(handler);
        while(true) {
            System.out.println("Waiting for tasks");
            Thread.sleep(5000);
        }
    }
}
