package utils;

import models.Event;
import models.EventType;

import java.io.*;
import java.text.ParseException;

/**
 * Created by ahmetkucuk on 04/10/15.
 */
public class EventFileReader {


    public static final String SEPARATOR = "\t";
    private static EventFileReader instance;
    private BufferedReader reader;
    private int eventTypeIndex;
    private int sdateIndex;
    private int edateIndex;
    private int channelIdIndex;
    private int polygonIndex;

    private int problematicRecord = 0;
    private int indexOfNextElement = 1;

    private EventFileReader(){}

    public static void init(String fileName) {
        if(instance != null)
            instance.onDestroy();
        instance = new EventFileReader();
        instance.loadFileContent(fileName);
    }

    public static EventFileReader getInstance() {
        if(instance == null) {
            System.err.println("You have to init before you call getInstance");
        }
        return instance;
    }

    private void loadFileContent(String inputFile) {
        FileInputStream fStream1 = null;
        try {
            fStream1 = new FileInputStream(inputFile);
            DataInputStream in = new DataInputStream(fStream1);
            reader = new BufferedReader(new InputStreamReader(in));
//
            String line = reader.readLine();
            eventTypeIndex = findIndexOfHeader(line, Constants.FieldNames.EVENT_TYPE);
            sdateIndex = findIndexOfHeader(line, Constants.FieldNames.START_DATE);
            edateIndex = findIndexOfHeader(line, Constants.FieldNames.END_DATE);
            channelIdIndex = findIndexOfHeader(line, Constants.FieldNames.CHANNEL_ID);
            polygonIndex = findIndexOfHeader(line, Constants.FieldNames.POLYGON);


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
            e.setId(indexOfNextElement);

            e.setStartDateString(columnValues[sdateIndex]);
            e.setStartDate(Utilities.getDateFromString(columnValues[sdateIndex]));

            e.setEndDateString(columnValues[edateIndex]);
            e.setEndDate(Utilities.getDateFromString(columnValues[edateIndex]));

            e.setEventType(EventType.fromString(columnValues[eventTypeIndex]));
            if(polygonIndex == -1) {
                System.out.println("No header value for polygon");
            }
            String polygonString = columnValues[polygonIndex];
            if(polygonIndex != -1 && polygonString.contains("POLYGON")) {
                e.setCoordinateString(polygonString.substring(9, polygonString.length() - 2));
            } else {
                System.out.println("Input does not contain Polygon String");
                problematicRecord++;
            }
            e.setMeasurement(columnValues[channelIdIndex]);

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

    public int findIndexOfHeader(String record, String columnName) {

        String[] headers = record.split(SEPARATOR);
        if(headers == null) {
            return -1;
        }

        for(int i = 0; i < headers.length; i++) {
            if(headers[i].equalsIgnoreCase(columnName)) return i;
        }

        return -1;
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
