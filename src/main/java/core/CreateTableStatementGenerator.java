package core;

import models.DBTable;
import models.EventType;
import utils.Constants;

import java.util.*;

/**
 * Created by ahmetkucuk on 22/11/15.
 */
public class CreateTableStatementGenerator {

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

    	for(DBTable t: DBTable.values()) {
            for(String query:createTableQuery(t)) {
                DBConnection.getInstance().executeCommand(query);
            }
        }
    	
        for(EventType e: EventType.values()) {
            for(String query:createTableQuery(e)) {
                DBConnection.getInstance().executeCommand(query);
                
            }
        }

        //After creation of tables are done, put index
        for(EventType e: EventType.values()) {
            DBConnection.getInstance().executeCommand(createIndexStatements(e.toString()));
        }

//        for(String triggerStatement:createTriggerStatements()) {
//        	DBConnection.getInstance().executeCommand(triggerStatement);
//        }
        
    }

    private List<String> createTriggerStatements() {
    	List<String> triggers = new ArrayList<String>();
    	for(EventType et: EventType.values()){
    		triggers.add( String.format(utils.Constants.TRIGGER.CREATE_ST_TABLE, et, et, et, et, et, et) );
    		triggers.add( String.format(utils.Constants.TRIGGER.CREATE_TRIGGER, et, et, et, et, et, et, et) );
    	}
		return triggers;
	}

	public List<String> createTableQuery(EventType eventType) {

        Map<String, String> map = GlobalAttributeHolder.getInstance().getAttributeDataTypeMap();
        Set<String> set = GlobalAttributeHolder.getInstance().getEventTypeByAttributes().get(eventType);

        return generateAttributeDataTypeMap(map, set, eventType.toString(), true);
    }

    private List<String> createTableQuery(DBTable dbTable) {

        Map<String, String> map = GlobalAttributeHolder.getInstance().getAttributeDataTypeMap();
        Set<String> set = GlobalAttributeHolder.getInstance().getDbTableByAttributes().get(dbTable);

        return generateAttributeDataTypeMap(map, set, dbTable.toString(), false);
    }

    private List<String> generateAttributeDataTypeMap(Map<String, String> map, Set<String> set, String tableName, boolean isEvent) {
        Map<String, String> genericEventColumns = new HashMap<>();
        Map<String, String> geometryColumns = new HashMap<>();
        for(String s: set) {
            if(GlobalAttributeHolder.getInstance().getGeometryAttribute().contains(s)) {
                geometryColumns.put(s, map.get(s) + "");
            } else {
                genericEventColumns.put(s, attributeByPOSTGREDataTypeMap.get(map.get(s) + ""));
            }
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
        if(isEvent) {
            builder.append(" PRIMARY KEY (kb_archivid),");
            builder.append(" CONSTRAINT argen_fk FOREIGN KEY (kb_archivid) REFERENCES  argen (kb_archivid),");
            builder.append(" CONSTRAINT frm_fk FOREIGN KEY (kb_archivid) REFERENCES  frm (kb_archivid),");
            builder.append(" CONSTRAINT intens_fk FOREIGN KEY (kb_archivid) REFERENCES  intens (kb_archivid),");
            builder.append(" CONSTRAINT obs_fk FOREIGN KEY (kb_archivid) REFERENCES  obs (kb_archivid)");
        } else {
            builder.append(" PRIMARY KEY (kb_archivid)");
        }
        builder.append(" );");
        if(isEvent) {
        }
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
