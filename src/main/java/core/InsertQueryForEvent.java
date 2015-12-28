package core;

import com.google.gson.JsonObject;
import models.DBTable;
import models.Event;
import utils.Constants;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by ahmetkucuk on 26/12/15.
 */
public class InsertQueryForEvent {

    private Event event;
    private List<String> insertQueries;

    public InsertQueryForEvent(Event e) {
        this.event = e;
        generateInsertQueries();
    }

    private void generateInsertQueries() {
        insertQueries = new ArrayList<>();

        
        for(DBTable dbTable:DBTable.values()) { //4 tables insert statements
        	Set<String> attributes = GlobalAttributeHolder.getInstance().getDbTableByAttributes().get(dbTable);
            List<String> values = new LinkedList<>();
            values.addAll(attributes.stream().map(attribute -> getActualPostgreMapping(event, attribute)).collect(Collectors.toList()));
            insertQueries.add(String.format(Constants.QUERY.INSERT_INTO, dbTable.toString(), String.join(",", attributes), String.join(",", values)));
        }
        
        //this is for actual insert to generic event tables
        List<String> values = new LinkedList<>();
        Set<String> attributes = GlobalAttributeHolder.getInstance().getEventTypeByAttributes().get(event.getEventType());
        values.addAll(attributes.stream().map(attribute -> getActualPostgreMapping(event, attribute)).collect(Collectors.toList()));
        
        insertQueries.add(String.format(Constants.QUERY.INSERT_INTO, event.getEventType().toString(), String.join(",", attributes), String.join(",", values)));
        
    }

    public String getActualPostgreMapping(Event event, String attribute) {

        Map<String, String> attributeDataTypeMap = GlobalAttributeHolder.getInstance().getAttributeDataTypeMap();

        if(GlobalAttributeHolder.getInstance().getGeometryAttribute().contains(attribute.toLowerCase())) {
            String attributeValue = event.get(attribute);
            if(attributeValue == null || attributeValue.isEmpty()) {
                return null;
            }
            return String.format(Constants.QUERY.GEOMETRY_FORM, event.get(attribute));
        }


        if(!attributeDataTypeMap.containsKey(attribute) || attributeDataTypeMap.get(attribute).equalsIgnoreCase("string") || attributeDataTypeMap.get(attribute).equalsIgnoreCase("timestamp")) {
            return "'" + (event.get(attribute) != null ? event.get(attribute).replaceAll("'", " ") : "") + "'";
        }

        String attributeValue = event.get(attribute);
        if(attributeValue == null || event.get(attribute).length() == 0) return "0";
        return event.get(attribute);
    }

    public List<String> getInsertQueries() {
        return insertQueries;
    }
}
