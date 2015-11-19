package utils;

import models.Event;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by ahmetkucuk on 01/10/15.
 */
public class Utilities {

    private static final String DELIMETER_COOR = ",";
    private static final String DELIMETER_POINT = " ";


    public static boolean isFileExists(String filePathString) {
        File f = new File(filePathString);
        return f.exists() && !f.isDirectory();
    }


    public static Date getDateFromString(String dateString) throws ParseException{

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-M-dd'T'HH:mm:ss");
        Date date = formatter.parse(dateString);
        return date;
    }

    public static String getStringFromDate(Date date) {

        SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        //SimpleDateFormat formatter2 = new SimpleDateFormat("HH:MM:SS");
        return formatter1.format(date);
    }

    public static List<Event> getEventByTime(String inputFile, String eventTime) throws ParseException {

        List result = new ArrayList();
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = dateFormatter.parse(eventTime);
        System.out.println(date);
        EventFileReader.init(inputFile);
        Event event = null;
        while((event = EventFileReader.getInstance().next()) != null) {
            if(event.getStartDate().getTime() == date.getTime()) {
                System.out.println("Time: " + event);
                result.add(event);
            }
        }

        return result;
    }


    static final Map<String, Integer> map = new HashMap<>();
    static final Map<String, Integer> mapofPossibleInputs = new HashMap<>();

    public static boolean isNumeric(String str)
    {
        return str.matches("-?\\d+(\\.\\d+)?");  //match a number with optional '-' and decimal.
    }

    public static boolean hasRealMeasurementValue(String measurement) {
        if(isNumeric(measurement)) return true;

        if(map.size() == 0)  {

            mapofPossibleInputs.put("AIA 171, AIA 193", 171);
            mapofPossibleInputs.put("AIA 193", 193);
            mapofPossibleInputs.put("131_THIN", 131);
            mapofPossibleInputs.put("94_THIN", 94);
            mapofPossibleInputs.put("131_THICK", 131);
            mapofPossibleInputs.put("94_THICK", 94);

        }
        return mapofPossibleInputs.containsKey(measurement);
    }
}
