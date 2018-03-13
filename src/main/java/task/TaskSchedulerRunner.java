package task;

import common.db.DBPrefs;
import common.db.JobRecordConnectionProvider;
import monitor.MonitoringRunner;
import task.core.TaskManager;

/**
 * Created by ahmetkucuk on 3/10/18.
 */
public class TaskSchedulerRunner {

    public static void main(String[] args) {
        System.out.println("Task Scheduler Runner Started!");
        DBPrefs.waitDefaultDBConnections();
        TaskManager.instance().startWithFixedRate();
        JobRecordConnectionProvider.connection().createJobRecordTable();
        MonitoringRunner.startServer();
    }
}
