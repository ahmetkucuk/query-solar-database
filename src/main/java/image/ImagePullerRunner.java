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
 */
public class ImagePullerRunner {

    final static String RABBIT_MQ_HOST = "rabbitmq";
    final static int RABBIT_MQ_PORT = 5672;
    final static String RABBIT_MQ_QUEUE_NAME = "IMAGE_PULLER";
    final static TaskQueue imagePullerQueue =  new TaskQueue(RABBIT_MQ_QUEUE_NAME, RABBIT_MQ_HOST, RABBIT_MQ_PORT);

    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.println("Image Puller Runner Started!");

        DBPrefs.waitDefaultDBConnections();

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

        while(true) {
            System.out.println("Waiting for tasks");
            Thread.sleep(5000);
        }
    }

    public static void downloadImages(ImagePullerTask task) {
        try {
            downloadImagesHelper(task);
        } catch (IOException | SQLException | SAXException e) {
            e.printStackTrace();
        }
    }

    public static void downloadImagesHelper(ImagePullerTask task) throws IOException, SAXException, SQLException {
        BufferedImage bi = null;

        DataSource ds = DBPrefs.getImageDataSource();
        IImageDBCreator dbCreator = new ImageDBCreator(DBPrefs.getImageDataSource());

        HelioviewerPullingImageDBConnection hwConnection = new HelioviewerPullingImageDBConnection(
                ds,
                dbCreator,
                null, 2);

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
