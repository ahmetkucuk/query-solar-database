package common.db;

import edu.gsu.dmlab.isd.monitor.IJobRecordConnection;
import edu.gsu.dmlab.isd.monitor.PostgresJobRecordConnection;

/**
 * Created by ahmetkucuk on 3/10/18.
 */
public class JobRecordConnectionProvider {
    private static JobRecordConnectionProvider instance;
    private PostgresJobRecordConnection monitorConnection;

    private JobRecordConnectionProvider() {
        monitorConnection = new PostgresJobRecordConnection(DBPrefs.getDataSource());
    }

    public static IJobRecordConnection connection() {
        if (instance == null) {
            instance = new JobRecordConnectionProvider();
        }
        return instance.monitorConnection;
    }
}
