package task.core;

import common.db.JobRecordConnectionProvider;
import edu.gsu.dmlab.isd.monitor.IJobRecordConnection;
import edu.gsu.dmlab.isd.mq.HEKPullerTask;
import edu.gsu.dmlab.isd.mq.TaskQueue;
import org.joda.time.DateTime;
import hek.utils.Utilities;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Created by ahmetkucuk on 2/19/18.
 */
public class TaskManager {

    private static final int TASK_CREATION_INTERVAL = 30;
    private static TaskManager instance;
    private final ScheduledExecutorService scheduler =
            Executors.newScheduledThreadPool(1);

    final static TaskQueue hekPullerQueue =  new TaskQueue("HEK_PULLER", "rabbitmq");
    final static TaskQueue imagePullerQueue =  new TaskQueue("IMAGE_PULLER", "rabbitmq");

    private ScheduledFuture<?> taskCreatorHandle;

    private IJobRecordConnection monitorConnection;

    private TaskManager() {
        monitorConnection = JobRecordConnectionProvider.connection();
    }

    public static TaskManager instance() {
        if (instance == null) {
            System.err.println("Init should be called before calling instance in TaskManager");
        }
        return instance;
    }

    public void startWithFixedRate() {

        final Runnable taskCreator = new Runnable() {
            public void run() {
                HEKPullerTask task = new HEKPullerTask(new DateTime(Utilities.getToday2AM()),
                        new DateTime(Utilities.getYesterday2AM()));

                if (task.createJobRecord(monitorConnection)) {
                    System.out.println("Job creation is successful");
                }

                // Simply send a task to download hek events
                // In our use case, tracking code can decide to send a pull request to our
                // hek puller before starting to track. Or, when hek puller finish pulling
                // it can send a tracking task request.
                hekPullerQueue.sendTask(task);
            }
        };
        taskCreatorHandle = scheduler.scheduleAtFixedRate(taskCreator, 0, TASK_CREATION_INTERVAL, TimeUnit.SECONDS);
    }
}
