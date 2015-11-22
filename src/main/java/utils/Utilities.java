package utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import models.Event;

import java.io.*;
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



    public static List<Event> convertFileToJSON(String fileName){

        // Read from File to String
        List<Event> eventList = new ArrayList<>();

        try(FileReader fileReader = new FileReader(fileName)) {
            JsonParser parser = new JsonParser();
            JsonElement jsonElement = parser.parse(fileReader);
            JsonArray eventsJson = jsonElement.getAsJsonObject().get("result").getAsJsonArray();
            for(int i = 0; i < eventsJson.size(); i++) {
                eventList.add(new Event(eventsJson.get(i).getAsJsonObject()));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return eventList;
    }

    public static String generateInsertQueryForAR(Event event) {

        List<String> values = new LinkedList<>();
        for(String s : Constants.QUERY.AR_ATTRIBUTE_LIST) {
            values.add(event.get(s));
        }
        return String.format(Constants.QUERY.INSERT_INTO_AR, String.join(",", values));

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
