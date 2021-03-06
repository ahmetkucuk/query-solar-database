package hek.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.apache.commons.lang3.time.DateUtils;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import hek.models.Event;

/**
 * Created by ahmetkucuk on 01/10/15.
 */
public class Utilities {

    private static final String DELIMETER_COOR = ",";
    private static final String DELIMETER_POINT = " ";


    public static Date getToday2AM() {

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 2);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        Date date = cal.getTime();
        return date;
    }

    public static Date getYesterday2AM() {

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 2);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        Date date = cal.getTime();
        date = DateUtils.addDays(date, -1);
        return date;

    }

    public static String getFileContent(String filePath) {
        String result = "";
        try(BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line = null;

            while((line = reader.readLine()) != null) {
                result = result + line;
            }
            return result;
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static Map<String, String> getAttributesMap(String attrFilePath) {

        Map<String, String> map = new HashMap<>();
        try(BufferedReader reader = new BufferedReader(new FileReader(attrFilePath))) {

            //Skip first line - header
            String line = reader.readLine();

            while((line = reader.readLine()) != null) {
                String[] columns = line.split("\t");
                map.put(columns[0], columns[1]);
            }
            return map;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    public static Map<String, String> getAttributesMap(InputStream attrFileInputStream) {

        Map<String, String> map = new HashMap<>();
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(attrFileInputStream))) {

            //Skip first line - header
            String line = reader.readLine();

            while((line = reader.readLine()) != null) {
                String[] columns = line.split("\t");
                map.put(columns[0], columns[1]);
            }
            return map;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    public static List<String> getPrivateAttrSet(String attrFilePath) {

        List<String> set = new ArrayList<>();
        try(BufferedReader reader = new BufferedReader(new FileReader(attrFilePath))) {

            String line = null;

            while((line = reader.readLine()) != null) {
                set.add(line.toLowerCase());
            }
            return set;
        }catch (Exception e) {
            e.printStackTrace();
        }
        return set;
    }

    public static Set<String> fileAsSet(String attrFilePath) {

        Set<String> set = new HashSet<>();
        try(BufferedReader reader = new BufferedReader(new FileReader(attrFilePath))) {
            String line = null;
            while((line = reader.readLine()) != null) {
                set.add(line);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return set;
    }

    public static Set<String> fileAsSet(InputStream attrFileInputStream) {

        Set<String> set = new HashSet<>();
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(attrFileInputStream))) {
            String line = null;
            while((line = reader.readLine()) != null) {
                set.add(line);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return set;
    }

    public static boolean isFileExists(String filePathString) {
        File f = new File(filePathString);
        return f.exists() && !f.isDirectory();
    }


    /**
     *
     * @param fileName
     * @param limit number of event to be extracted from json file
     * @return
     */
    public static List<Event> convertFileToJSON(String fileName, int limit){

        // Read from File to String
        List<Event> eventList = new ArrayList<>();
        System.out.println(fileName);
        try(FileReader fileReader = new FileReader(fileName)) {
            JsonParser parser = new JsonParser();
            JsonElement jsonElement = parser.parse(fileReader);
            JsonArray eventsJson = jsonElement.getAsJsonObject().get("result").getAsJsonArray();
            for(int i = 0; i < eventsJson.size() && i < limit; i++) {
                eventList.add(new Event(eventsJson.get(i).getAsJsonObject()));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return eventList;
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

    public static boolean isNumeric(String str)
    {
        return str.matches("-?\\d+(\\.\\d+)?");  //match a number with optional '-' and decimal.
    }
}
