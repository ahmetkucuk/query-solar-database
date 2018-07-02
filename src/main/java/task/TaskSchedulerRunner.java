package task;

import common.db.DBPrefs;
import common.db.JobRecordConnectionProvider;
import monitor.MonitoringRunner;
import task.core.TaskManager;

/**
 * Class that attempts to connect to Default DB until connection is successful.
 * Task manager is started and sends tasks to HEK puller and image puller and
 * creates a JobRecord object in Monitoring DB every 30 seconds. Monitoring App
 * is started, which creates an HTTP server on port 1278. The app provides a
 * list of JobRecords created in the DB.
 * 
 * @author - ahmetkucuk
 * @author - kqian5
 */
public class TaskSchedulerRunner {

	/**
	 * Main method that attempts to connect to Default DB until connection is
	 * successful. Task manager is started and sends tasks to HEK puller and image
	 * puller and creates a JobRecord object in Monitoring DB every 30 seconds.
	 * Monitoring App is started, which creates an HTTP server on port 1278. The app
	 * provides a list of JobRecords created in the DB.
	 * @param args - user inputs
	 */
	public static void main(String[] args) {
		System.out.println("Task Scheduler Runner Started!");
		DBPrefs.waitDefaultDBConnections();
		TaskManager.instance().startWithFixedRate();
		JobRecordConnectionProvider.connection().createJobRecordTable();
		MonitoringRunner.startServer();
	}
}
