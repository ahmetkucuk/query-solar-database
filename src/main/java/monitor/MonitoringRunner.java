package monitor;

import common.db.JobRecordConnectionProvider;
import task.core.TaskManager;
import edu.gsu.dmlab.isd.monitor.JobRecord;
import edu.gsu.dmlab.isd.mq.Utils;
import fi.iki.elonen.NanoHTTPD;
import org.joda.time.DateTime;

import java.io.IOException;
import java.util.List;

/**
 * Created by ahmetkucuk on 3/8/18.
 */
public class MonitoringRunner extends NanoHTTPD{

    public MonitoringRunner() throws IOException {
        super(1278);
        start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
        System.out.println("\nRunning! Point your browsers to http://localhost:1278/ \n");
    }

    public static void startServer() {
        try {
            new MonitoringRunner();
        } catch (IOException ioe) {
            System.err.println("Couldn't start server:\n" + ioe);
        }
    }

    @Override
    public Response serve(IHTTPSession session) {
        DateTime fromDate = DateTime.now().minusDays(10);
        List<JobRecord> records = JobRecordConnectionProvider.connection().getJobRecordsFromDate(fromDate);
        String json = Utils.getJodoAwareGson().toJson(records);
        return newFixedLengthResponse(NanoHTTPD.Response.Status.OK, "application/json", json);
    }
}
