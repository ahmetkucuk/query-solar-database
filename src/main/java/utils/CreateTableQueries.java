package utils;

import core.DBConnection;
import models.Event;
import models.EventType;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.SynchronousQueue;

/**
 * Created by ahmetkucuk on 22/11/15.
 */
public class CreateTableQueries {

    public static final String ATTRIBUTE_BASE = "/Users/ahmetkucuk/Documents/GEORGIA_STATE/COURSES/Database_Systems/Project/Data/";
    public static final String ATTRIBUTE_LIST = ATTRIBUTE_BASE + "Parameter_Types.txt";
    public static final String EVENT_ATTRIBUTE_LIST = ATTRIBUTE_BASE + "%sprivate.txt";

    private static final Map<String, String> attributeByPOSTGREDataTypeMap = new HashMap<>();
    private static final Map<String, String> specialAttributes = new HashMap<>();
    static {
        attributeByPOSTGREDataTypeMap.put("float", "float");
        attributeByPOSTGREDataTypeMap.put("string", "text");
        attributeByPOSTGREDataTypeMap.put("integer", "integer");
        attributeByPOSTGREDataTypeMap.put("long", "bigint");
        attributeByPOSTGREDataTypeMap.put("null", "text");

        specialAttributes.put("event_starttime", "TIMESTAMP");
        specialAttributes.put("event_endtime", "TIMESTAMP");
    }

    public void createTables() {

        for(EventType e: EventType.values()) {
            String query = CreateTableQueries.createTableQuery(ATTRIBUTE_LIST, String.format(EVENT_ATTRIBUTE_LIST, e.toString()), e);
            boolean result = new DBConnection().executeCommand(query);
            System.out.println(query);
        }
    }

    public static String createTableQuery(String attributeList, String subAttributeList, EventType eventType) {

        Map<String, String> map = getAttributesMap(attributeList);
        Set<String> set = getPrivateAttrSet(subAttributeList);

        Map<String, String> arMap = new HashMap<>();
        for(String s: set) {
            String valueDataType = specialAttributes.get(s);

            if(valueDataType == null) {
                valueDataType = attributeByPOSTGREDataTypeMap.get(map.get(valueDataType) + "");
            }
            arMap.put(s, valueDataType);
        }
        return createQueryUsingMap(arMap, eventType);
    }

    public static String createQueryUsingMap(Map<String, String> map, EventType eventType) {
        StringBuilder builder = new StringBuilder();
        builder.append("DROP TABLE IF EXISTS " + eventType.toString() + ";");
        builder.append("CREATE TABLE ");
        builder.append(eventType.toString());
        builder.append(" ( ");
        builder.append("kb_archiveid varchar(20),");
        for(Map.Entry<String, String> entry : map.entrySet()) {
            builder.append(entry.getKey() + " " + entry.getValue() + ", ");
        }
        builder.append(" PRIMARY KEY (kb_archiveid)");
        builder.append(" );");
        return builder.toString();
    }

    public static Map<String, String> getAttributesMap(String attrFilePath) {

        Map<String, String> map = new HashMap<>();
        try(BufferedReader reader = new BufferedReader(new FileReader(attrFilePath))) {

            String line = reader.readLine();

            while((line = reader.readLine()) != null) {
                String[] columns = line.split("\t");
                map.put(columns[0].toLowerCase(), columns[1].toLowerCase());
            }
            return map;
        }catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    public static Set<String> getPrivateAttrSet(String attrFilePath) {

        Set<String> set = new HashSet<>();
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
}
