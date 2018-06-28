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
 * Class that acts as an embeddable HTTP server
 * that can return a list of JobRecord objects from
 * the database formatted in json
 * @author - ahmetkucuk
 * @author - kqian5
 */
public class MonitoringRunner extends NanoHTTPD{

	/**
	 * Constructor for a MonitoringRunner object
	 * Starts the server to port 1278 and waits for a maximum
	 * of 5000 milliseconds for socket to read
	 * @throws IOException - thrown if error starting server
	 */
    public MonitoringRunner() throws IOException {
        super(1278);
        start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
        System.out.println("\nRunning! Point your browsers to http://localhost:1278/ \n");
    }

    /**
     * Method that constructs a new MonitoringRunner object
     * and catches an exception if server fails to start
     */
    public static void startServer() {
        try {
            new MonitoringRunner();
        } catch (IOException ioe) {
            System.err.println("Couldn't start server:\n" + ioe);
        }
    }

    /**
     * Method that looks up a list of JobRecord objects in the DB
     * for a specific date(10 days prior to request time) and 
     * formats an appropriate response in json
     */
    @Override
    public Response serve(IHTTPSession session) {
        DateTime fromDate = DateTime.now().minusDays(10);
        List<JobRecord> records = JobRecordConnectionProvider.connection().getJobRecordsFromDate(fromDate);
        String json = Utils.getJodoAwareGson().toJson(records);
        return newFixedLengthResponse(NanoHTTPD.Response.Status.OK, "application/json", json);
    }
}
