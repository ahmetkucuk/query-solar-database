package hek.core;

import hek.models.EventType;
import hek.utils.Constants;

import java.util.*;

/**
 * Created by ahmetkucuk on 22/11/15.
 */
public class TableCreator {

    private static final Map<String, String> attributeByPOSTGREDataTypeMap = new HashMap<>();
    static {
        attributeByPOSTGREDataTypeMap.put("float", "float");
        attributeByPOSTGREDataTypeMap.put("string", "text");
        attributeByPOSTGREDataTypeMap.put("integer", "integer");
        attributeByPOSTGREDataTypeMap.put("long", "bigint");
        attributeByPOSTGREDataTypeMap.put("null", "text");
        attributeByPOSTGREDataTypeMap.put("timestamp", "timestamp");
    }

    DBConnection connection;

    public TableCreator(DBConnection connection) {
        this.connection = connection;
    }

    public void createTables() {

        connection.executeCommand("CREATE EXTENSION postgis;");

        for(EventType e: EventType.values()) {
            for(String query:createTableQuery(e)) {
                connection.executeCommand(query);
            }
        }

        //After creation of tables are done, put index
        for(EventType e: EventType.values()) {
            String indexQuery = createIndexStatements(e.toString());
            connection.executeCommand(indexQuery);
        }

//        for(String triggerStatement:createTriggerStatements()) {
//        	DBConnection.getInstance().executeCommand(triggerStatement);
//        }
        connection.closeConnection();

    }

    private List<String> createTriggerStatements() {
    	List<String> triggers = new ArrayList<String>();
    	for(EventType et: EventType.values()){
    		triggers.add( String.format(hek.utils.Constants.TRIGGER.CREATE_ST_TABLE, et, et, et, et, et, et) );
    		triggers.add( String.format(hek.utils.Constants.TRIGGER.CREATE_TRIGGER, et, et, et, et, et, et, et) );
    	}
		return triggers;
	}

	public List<String> createTableQuery(EventType eventType) {

        Map<String, String> map = EventAttributeManager.getInstance().getAttributeDataTypeMap();
        Set<String> set = EventAttributeManager.getInstance().getEventTypeByAttributes().get(eventType);

        return generateAttributeDataTypeMap(map, set, eventType.toString(), true);
    }


    private List<String> generateAttributeDataTypeMap(Map<String, String> map, Set<String> set, String tableName, boolean isEvent) {
        Map<String, String> genericEventColumns = new HashMap<>();
        Map<String, String> geometryColumns = new HashMap<>();
        for(String s: set) {
            genericEventColumns.put(s, attributeByPOSTGREDataTypeMap.get(map.get(s) + ""));
        }
        for (String geom :EventAttributeManager.geometryAttributes) {
            geometryColumns.put(geom, map.get(geom) + "");
        }


        //Query to create table
        String result = createActualStatement(genericEventColumns, tableName, isEvent);

        //List of query to add spatial columns
        List<String> addStatements = createAddGeometryStatement(geometryColumns, tableName);

        //execute create table first
        addStatements.add(0, result);
        return addStatements;
    }

    private String createActualStatement(Map<String, String> map, String tableName, boolean isEvent) {
        StringBuilder builder = new StringBuilder();
        //DROP TABLE " + tableName + " CASCADE;
        //builder.append("DROP TABLE IF EXISTS " + tableName + " CASCADE;");
        builder.append("CREATE TABLE IF NOT EXISTS ");
        builder.append(tableName);
        builder.append(" ( ");
        for(Map.Entry<String, String> entry : map.entrySet()) {
            builder.append(entry.getKey() + " " + entry.getValue() + ", ");
        }
        builder.append(" PRIMARY KEY (kb_archivid)");
        builder.append(" );");

        return builder.toString();
    }

    public String createIndexStatements(String tableName) {

        String index = "CREATE INDEX ON %s USING btree ( tsrange(event_starttime, event_endtime, '[]') );CREATE INDEX %s_hpc_bbox_idx ON %s USING gist (hpc_bbox);";
        return String.format(index, tableName, tableName, tableName);
    }

    public List<String> createAddGeometryStatement(Map<String, String> map, String tableName) {

        List<String> listOfAddGeometryColumns = new ArrayList<>();
        for(String s: map.keySet()) {
            listOfAddGeometryColumns.add(String.format(Constants.QUERY.ADD_GEOMETRY_COLUMN, tableName.toLowerCase(), s, map.get(s)));
        }
        return listOfAddGeometryColumns;
    }

}
