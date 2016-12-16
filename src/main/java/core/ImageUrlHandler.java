package core;

import models.ImageAttributes;
import temp.Event;
import temp.EventReader;
import temp.FileWriter;
import utils.ImageListParser;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ahmetkucuk on 05/04/16.
 */
public class ImageUrlHandler {


    private static final String SECONDARY_TABLENAME = "secondary_images";
    private static final String PRIMARY_TABLENAME = "primary_images";

    public static final String NEAREST_IMAGE_TIME = "SELECT name FROM %s WHERE measurement like '%s' ORDER BY GREATEST('%s'::timestamp,  date) - LEAST('%s'::timestamp,  date) LIMIT 1;";


    public static String sendDBQuery(Date date, String measurement, String tableName) {
        DBConnection connection = DBConnection.getNewConnection();
        String result = connection.getFileName(String.format(NEAREST_IMAGE_TIME, tableName, measurement, date, date));
        connection.closeConnection();
        return result;
    }

    public static void retrieveClosestImageNames() {

        EventReader reader = new EventReader("/Users/ahmetkucuk/Documents/Developer/java/QueryHEK/Result/All/primary.txt");

        FileWriter writer = new FileWriter("/Users/ahmetkucuk/Documents/Research/EVENTS_DATASET/HPC/primary.txt");
        writer.start();
        writer.writeToFile("id\tevent_type\tstart_time\tend_time\tchannel\tbbox\tsfilename\tmfilename\tefilename\n");

        String tableName = PRIMARY_TABLENAME;
        Event event;
        int counter = 0;
        Map<String, String> timeByImageName = new HashMap<>();
        while((event = reader.next()) != null) {

            String key = event.getStartDate().toString() + event.getMeasurement();
            if(timeByImageName.containsKey(key)) {
                event.setsFileName(timeByImageName.get(key));
            } else {
                String fileName = sendDBQuery(event.getStartDate(), event.getMeasurement(), tableName);
                timeByImageName.put(key, fileName);
                event.setsFileName(fileName);
            }

            key = event.getMiddleDate().toString() + event.getMeasurement();
            if(timeByImageName.containsKey(key)) {
                event.setmFileName(timeByImageName.get(key));
            } else {
                String fileName = sendDBQuery(event.getMiddleDate(), event.getMeasurement(), tableName);
                timeByImageName.put(key, fileName);
                event.setmFileName(fileName);
            }

            key = event.getEndDate().toString() + event.getMeasurement();
            if(timeByImageName.containsKey(key)) {
                event.seteFileName(timeByImageName.get(key));
            } else {
                String fileName = sendDBQuery(event.getEndDate(), event.getMeasurement(), tableName);
                timeByImageName.put(key, fileName);
                event.seteFileName(fileName);
            }

            writer.writeToFile(event.toString() + "\n");
            if(counter % 5000 == 0) {
                System.out.println(counter);
                System.out.println("Map Size: " + timeByImageName.size());
            }
            counter++;
        }
        System.out.println("Map Size: " + timeByImageName.size());
        writer.finish();

    }

    public static void insertImageFileNamesToDatabase (String imageFileName, String imageType){
        ImageListParser parser = new ImageListParser(imageFileName, imageType);
        ImageAttributes imageAttributes;
        DBConnection connection = DBConnection.getNewConnection();
        while((imageAttributes = parser.next()) != null) {
            connection.executeCommand(imageAttributes.getInsertQuery());
            //System.out.println(imageAttributes.getInsertQuery());
        }
        connection.closeConnection();
    }
}
