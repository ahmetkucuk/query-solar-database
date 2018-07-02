package task.core;

import common.db.JobRecordConnectionProvider;
import edu.gsu.dmlab.isd.monitor.IJobRecordConnection;
import edu.gsu.dmlab.isd.mq.HEKPullerTask;
import edu.gsu.dmlab.isd.mq.ImagePullerTask;
import edu.gsu.dmlab.isd.mq.TaskQueue;
import org.joda.time.DateTime;
import hek.utils.Utilities;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Class that sends tasks to HEK puller and Image puller, and adds
 * a JobRecord object in the Monitoring DB every 30 seconds.
 * @author - ahmetkucuk
 * @author - kqian5
 */
public class TaskManager {

	//TODO - create a method that stops startWithFixedRate() method
	
    private static final int TASK_CREATION_INTERVAL = 30;
    private static TaskManager instance;
    private final ScheduledExecutorService scheduler =
            Executors.newScheduledThreadPool(1);

    final static TaskQueue hekPullerQueue =  new TaskQueue("HEK_PULLER", "rabbitmq");
    final static TaskQueue imagePullerQueue =  new TaskQueue("IMAGE_PULLER", "rabbitmq");

    private ScheduledFuture<?> taskCreatorHandle;

    private IJobRecordConnection monitorConnection;

    /**
     * Constructor for a TaskManager object that initializes 
     * a connection to the Monitoring DB
     */
    private TaskManager() {
        monitorConnection = JobRecordConnectionProvider.connection();
    }

    /**
     * Method that returns an instance of a TaskManager
     * @return - an instance of a TaskManager object
     */
    public static TaskManager instance() {
        if (instance == null) {
            instance = new TaskManager();
        }
        return instance;
    }

    /**
     * Method that creates a new HEK puller task, image puller task, 
     * and job record in Monitoring DB every 30 seconds. These tasks
     * are created in the background through different threads.
     * 
     */
    public void startWithFixedRate() {
        final Runnable taskCreator = new Runnable() {
            public void run() {
                HEKPullerTask task = new HEKPullerTask(new DateTime(Utilities.getYesterday2AM()),
                        new DateTime(Utilities.getToday2AM()));

                if (task.createJobRecord(monitorConnection)) {
                    System.out.println("Job creation is successful");
                }

                // Simply send a task to download hek events
                // In our use case, tracking code can decide to send a pull request to our
                // hek puller before starting to track. Or, when hek puller finish pulling
                // it can send a tracking task request.
                hekPullerQueue.sendTask(task);

                DateTime yesterday = new DateTime(Utilities.getYesterday2AM());
                ImagePullerTask imagePullerTask = new ImagePullerTask(yesterday.minusMinutes(10), yesterday);
                imagePullerQueue.sendTask(imagePullerTask);
            }
        };
        taskCreatorHandle = scheduler.scheduleAtFixedRate(taskCreator, 0, TASK_CREATION_INTERVAL, TimeUnit.SECONDS);
    }
}	