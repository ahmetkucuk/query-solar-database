package image;

import common.db.DBPrefs;
import common.db.JobRecordConnectionProvider;
import edu.gsu.dmlab.databases.HelioviewerPullingImageDBConnection;
import edu.gsu.dmlab.databases.ImageDBCreator;
import edu.gsu.dmlab.databases.interfaces.IImageDBCreator;
import edu.gsu.dmlab.datatypes.Waveband;
import edu.gsu.dmlab.isd.monitor.JobStatus;
import edu.gsu.dmlab.isd.mq.ImagePullerTask;
import edu.gsu.dmlab.isd.mq.TaskHandler;
import edu.gsu.dmlab.isd.mq.TaskQueue;
import org.joda.time.Interval;
import org.xml.sax.SAXException;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import javax.imageio.stream.MemoryCacheImageOutputStream;
import javax.sql.DataSource;
import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by ahmetkucuk on 3/10/18.
 * @author ahmetkucuk
 * @author jookimmy
 */
public class ImagePullerRunner {


    /**
     * Variable to assign name of Host to use for TaskQueue function.
     */
    final static String RABBIT_MQ_HOST = "rabbitmq";

    /**
     * Port number assignment to final variable to use for TaskQueue function.
     */
    final static int RABBIT_MQ_PORT = 5672;

    /**
     * Name of module/task to access when using the TaskQueue function.
     */
    final static String RABBIT_MQ_QUEUE_NAME = "IMAGE_PULLER";

    /**
     * Making the call of TaskQueue using parameters defined above.
     */
    final static TaskQueue imagePullerQueue =  new TaskQueue(RABBIT_MQ_QUEUE_NAME, RABBIT_MQ_HOST, RABBIT_MQ_PORT);

    /**
     * Main class to use the Image Puller Module.
     * @param args - standard array of string arguments that can be passed to application
     * @throws IOException - input/output exception
     * @throws InterruptedException - when thread is interrupted either during or before activity
     */
    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.println("Image Puller Runner Started!");

        //Establishes database connections
        DBPrefs.waitDefaultDBConnections();

        //Creates a new instance of "TaskHandling" ImagePuller tasks as handler
        TaskHandler<ImagePullerTask> handler = new TaskHandler<ImagePullerTask>(ImagePullerTask.class) {
            public void handleTask(ImagePullerTask task) {
                //Task Arrived, change job status: Processing
                JobRecordConnectionProvider.connection().setJobStatus(task.getJobID(), JobStatus.PROCESSING, null);

                // Pull Events in here
                System.out.println("Handling Image Task: " + task);
                downloadImages(task);

                //Task completed, change job status: Completed
                // If tasks fail change status to failed and add exception
                JobRecordConnectionProvider.connection().setJobStatus(task.getJobID(), JobStatus.COMPLETED, null);
            }
        };
        imagePullerQueue.registerConsumer(handler);

        //Currently set to run constantly
        while(true) {
            System.out.println("Waiting for tasks");
            //Timer set to 5 second intervals
            Thread.sleep(5000);
        }
    }

    /**
     * Method using the DI helper to download images from input task.
     * @param task - the task given to the IPR
     */
    public static void downloadImages(ImagePullerTask task) {
        try {
            //uses the helper to complete task.
            downloadImagesHelper(task);

          //Exceptions to catch
        } catch (IOException | SQLException | SAXException e) {
            e.printStackTrace();
        }
    }

    /**
     * The function used in the downloadImages method above.
     * @param task - task given to the IPR
     * @throws IOException - thrown when there is an input/output exception in the task
     * @throws SAXException - XML parsing exception
     * @throws SQLException - an exception that provides information on a database access error or other errors.
     */
    public static void downloadImagesHelper(ImagePullerTask task) throws IOException, SAXException, SQLException {
        //initialized class object buffered image
        BufferedImage bi = null;

        //Data source acquired using DBPrefs class
        DataSource ds = DBPrefs.getImageDataSource();

        //Interface instance of ImageDBCreator
        IImageDBCreator dbCreator = new ImageDBCreator(DBPrefs.getImageDataSource());

        //Using previously initialized instances of data source, and database creators to use Helioviewer puller.
        HelioviewerPullingImageDBConnection hwConnection = new HelioviewerPullingImageDBConnection(
                ds,
                dbCreator,
                null, 2);

        //Setting interval of startTime and endTime
        Interval interval = new Interval(task.startTime, task.endTime);

        // Get the first image within the interval from DMLAB database
        bi = hwConnection.getFirstImage(interval, Waveband.AIA131);
        System.out.println(bi.getHeight());

        // Get the first image within the interval from DMLAB database,
        // if not found, pull from helioviewer
        bi = hwConnection.getFirstFullImage(interval, Waveband.AIA131);

        FileOutputStream output = new FileOutputStream("out.jp2");
        ImageOutputStream out = new MemoryCacheImageOutputStream(output);
        // get an image writer for jpg images
        ImageWriter writer = ImageIO.getImageWritersByFormatName("jp2").next();
        writer.setOutput(out);
    }
}
