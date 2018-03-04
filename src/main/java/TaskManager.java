import edu.gsu.dmlab.isd.monitor.IJobRecordConnection;
import edu.gsu.dmlab.isd.monitor.JobRecord;
import edu.gsu.dmlab.isd.monitor.JobStatus;
import edu.gsu.dmlab.isd.mq.HEKPullerTask;
import edu.gsu.dmlab.isd.mq.TaskQueue;
import org.joda.time.DateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Created by ahmetkucuk on 2/19/18.
 */
public class TaskManager {

    private static final int TASK_CREATION_INTERVAL = 30;
    private static final TaskManager instance = new TaskManager();
    private final ScheduledExecutorService scheduler =
            Executors.newScheduledThreadPool(1);

    final static TaskQueue queue =  new TaskQueue("abc", "rabbitmq");
    private ScheduledFuture<?> taskCreatorHandle;

    // This will be replaced by actual DBConnection
    public static final IJobRecordConnection mockConnection = new IJobRecordConnection() {

        @Override
        public boolean createJobRecordTable() { return false; }

        @Override
        public JobRecord getRecord(long id) { return null; }

        @Override
        public long insertRecord(JobRecord record) {
            System.out.println("Job is inserted to db table");
            return 10;
        }

        @Override
        public boolean setJobStatus(long id, JobStatus status, String exception) {
            System.out.println("Job " + id + " : status updated to: " + status);
            if (exception != null) {
                System.out.println("Job " + id + " FAILED with exception: " + exception);
            }
            return true;
        }
    };

    private TaskManager() {

    }

    public static TaskManager getInstance() {
        return instance;
    }


    public void startWithFixedRate() {

        final Runnable taskCreator = new Runnable() {
            public void run() {
                HEKPullerTask task = new HEKPullerTask(new DateTime(), new DateTime());

                if (task.createJobRecord(mockConnection)) {
                    System.out.println("Job creation is successful");
                }

                // Simply send a task to download hek events
                // In our use case, tracking code can decide to send a pull request to our
                // hek puller before starting to track. Or, when hek puller finish pulling
                // it can send a tracking task request.
                queue.sendTask(task);
            }
        };
        taskCreatorHandle = scheduler.scheduleAtFixedRate(taskCreator, 0, TASK_CREATION_INTERVAL, TimeUnit.SECONDS);
    }
}
