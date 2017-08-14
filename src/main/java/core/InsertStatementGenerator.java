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
public class InsertStatementGenerator {

    private Event event;
    private List<String> insertQueries;

    public InsertStatementGenerator(Event e) {
        this.event = e;
        generateInsertQueries();
    }

    private void generateInsertQueries() {
        insertQueries = new ArrayList<>();

        
        //this is for actual insert to generic event tables
        List<String> values = new LinkedList<>();
        Set<String> attributes = EventAttributeManager.getInstance().getEventTypeByAttributes().get(event.getEventType());

        Set<String> allAttributes = new HashSet<>(attributes);
        allAttributes.addAll(EventAttributeManager.geometryAttributes);

        values.addAll(allAttributes.stream().map(attribute -> getActualPostgreMapping(event, attribute)).collect(Collectors.toList()));
        
        insertQueries.add(String.format(Constants.QUERY.INSERT_INTO, event.getEventType().toString(), String.join(",", allAttributes), String.join(",", values)));
        
    }

    public String getActualPostgreMapping(Event event, String attribute) {

        Map<String, String> attributeDataTypeMap = EventAttributeManager.getInstance().getAttributeDataTypeMap();

        if(EventAttributeManager.geometryAttributes.contains(attribute.toLowerCase())) {
            String attributeValue = event.get(attribute);
            if(attributeValue == null || attributeValue.isEmpty()) {
                return null;
            }
            return String.format(Constants.QUERY.GEOMETRY_FORM, event.get(attribute));
        }


        if(attribute.equalsIgnoreCase("event_starttime")) {
            return "'" + (event.get(attribute) != null ? event.get(attribute).replaceAll("'", " ") : "") + "'";
        } else if(attribute.equalsIgnoreCase("event_endtime")) {
            if(event.get("event_endtime").equalsIgnoreCase(event.get("event_starttime"))) {
                return "TIMESTAMP '" + (event.get(attribute) != null ? event.get(attribute).replaceAll("'", " ") : "") + "' + INTERVAL '1 second'";
            }
            return "'" + (event.get(attribute) != null ? event.get(attribute).replaceAll("'", " ") : "") + "'";
        }

        if(!attributeDataTypeMap.containsKey(attribute) || attributeDataTypeMap.get(attribute).equalsIgnoreCase("string")) {
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
