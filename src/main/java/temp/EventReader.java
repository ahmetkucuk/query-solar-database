package temp;

import utils.Utilities;

import java.io.*;
import java.text.ParseException;

/**
 * Created by ahmetkucuk on 01/11/15.
 */
public class EventReader {


    public static final String SEPARATOR = "\t";
    private BufferedReader reader;
    private int eventIdIndex;
    private int eventTypeIndex;
    private int sdateIndex;
    private int edateIndex;
    private int channelIdIndex;
    private int polygonIndex;
    private int sFileName;
    private int mFileName;
    private int eFileName;

    private int problematicRecord = 0;
    private int indexOfNextElement = 1;

    public EventReader(String fileName) {
        loadFileContent(fileName);
    }

    private void loadFileContent(String inputFile) {
        FileInputStream fStream1 = null;
        try {
            fStream1 = new FileInputStream(inputFile);
            DataInputStream in = new DataInputStream(fStream1);
            reader = new BufferedReader(new InputStreamReader(in));
//
            //Skip header
            String line = reader.readLine();
            eventIdIndex = 0;
            eventTypeIndex = 1;
            sdateIndex = 2;
            edateIndex = 3;
            channelIdIndex = 4;
            polygonIndex = 5;
            sFileName = 6;
            mFileName = 7;
            eFileName = 8;



        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Event next() {
        String line = null;
        try {
            line = reader.readLine();
            if(line == null) return null;

            String[] columnValues = line.split(SEPARATOR);
            Event e = new Event();
            e.setId(columnValues[eventIdIndex]);

            e.setStartDateString(columnValues[sdateIndex]);
            e.setStartDate(Utilities.getDateFromString(columnValues[sdateIndex]));

            e.setEndDateString(columnValues[edateIndex]);
            e.setEndDate(Utilities.getDateFromString(columnValues[edateIndex]));

            e.setEventType(EventType.fromString(columnValues[eventTypeIndex]));
            if(polygonIndex == -1) {
                System.out.println("No header value for polygon");
            }
            String polygonString = columnValues[polygonIndex];
            e.setCoordinateString(polygonString);
            e.setMeasurement(columnValues[channelIdIndex]);

            if(columnValues.length > 6) {
                e.setsFileName(columnValues[sFileName]);
                e.setmFileName(columnValues[mFileName]);
                e.seteFileName(columnValues[eFileName]);
            }

            indexOfNextElement++;

            return e;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            System.out.println(indexOfNextElement + " : " + line);
            e.printStackTrace();
        }
        return null;
    }

    private void onDestroy() {
        try {
            if(reader != null)
                reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public String getStats() {
        return "problem: " + problematicRecord + " total index: " + indexOfNextElement;
    }

}
