package core;

import com.sun.deploy.util.StringUtils;
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
    static {
        attributeByPOSTGREDataTypeMap.put("float", "float");
        attributeByPOSTGREDataTypeMap.put("string", "text");
        attributeByPOSTGREDataTypeMap.put("integer", "integer");
        attributeByPOSTGREDataTypeMap.put("long", "bigint");
        attributeByPOSTGREDataTypeMap.put("null", "text");
        attributeByPOSTGREDataTypeMap.put("timestamp", "timestamp");
    }

    public void createTables() {

        for(EventType e: EventType.values()) {
            for(String query:createTableQuery(e)) {
                DBConnection.getInstance().executeCommand(query);
            }
        }
    }

    public List<String> createTableQuery(EventType eventType) {

        Map<String, String> map = GlobalAttributeHolder.getInstance().getAttributeDataTypeMap();
        Set<String> set = GlobalAttributeHolder.getInstance().getEventTypeByAttributes().get(eventType);

        Map<String, String> genericEventColumns = new HashMap<>();
        Map<String, String> geometryColumns = new HashMap<>();
        for(String s: set) {
            if(GlobalAttributeHolder.getInstance().getGeometryAttribute().contains(s)) {
                geometryColumns.put(s, map.get(s) + "");
            } else {
                genericEventColumns.put(s, attributeByPOSTGREDataTypeMap.get(map.get(s) + ""));
            }
        }
        String result = createActualStatement(genericEventColumns, eventType);
        List<String> addStatements = createAddGeometryStatement(geometryColumns, eventType);
        addStatements.add(0, result);
        return addStatements;
    }

    public String createActualStatement(Map<String, String> map, EventType eventType) {
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

    public List<String> createAddGeometryStatement(Map<String, String> map, EventType eventType) {

        List<String> listOfAddGeometryColumns = new ArrayList<>();
        for(String s: map.keySet()) {
            listOfAddGeometryColumns.add(String.format(Constants.QUERY.ADD_GEOMETRY_COLUMN, eventType.toString().toLowerCase(), s, map.get(s)));
        }
        return listOfAddGeometryColumns;
    }

}
