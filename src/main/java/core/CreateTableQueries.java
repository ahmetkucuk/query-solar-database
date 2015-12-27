package core;

import models.EventType;
import utils.Constants;
import utils.FileManager;
import utils.Utilities;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

/**
 * Created by ahmetkucuk on 22/11/15.
 */
public class CreateTableQueries {

    public static final String ATTRIBUTE_BASE = "/Users/ahmetkucuk/Documents/GEORGIA_STATE/COURSES/Database_Systems/Project/Data/";

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
            String actualFilePath = String.format(Constants.DATA.EVENT_ATTRIBUTE_LIST_BASE, e.toString());
            String query = createTableQuery(e);
            boolean result = DBConnection.getInstance().executeCommand(query);
            System.out.println(query);
        }
    }

    public String createTableQuery(EventType eventType) {

        Map<String, String> map = GlobalAttributeHolder.getInstance().getAttributeDataTypeMap();
        Set<String> set = GlobalAttributeHolder.getInstance().getEventTypeByAttributes().get(eventType);

        Map<String, String> eventMap = new HashMap<>();
        for(String s: set) {
            String valueDataType = specialAttributes.get(s);

            if(valueDataType == null) {
                valueDataType = attributeByPOSTGREDataTypeMap.get(map.get(valueDataType) + "");
            }
            eventMap.put(s, valueDataType);
        }
        return createQueryUsingMap(eventMap, eventType);
    }

    public String createQueryUsingMap(Map<String, String> map, EventType eventType) {
        StringBuilder builder = new StringBuilder();
        builder.append("DROP TABLE IF EXISTS " + eventType.toString() + ";");
        builder.append("CREATE TABLE ");
        builder.append(eventType.toString());
        builder.append(" ( ");
        for(Map.Entry<String, String> entry : map.entrySet()) {
            builder.append(entry.getKey() + " " + entry.getValue() + ", ");
        }
        builder.append(" PRIMARY KEY (kb_archivid)");
        builder.append(" );");
        return builder.toString();
    }

}
