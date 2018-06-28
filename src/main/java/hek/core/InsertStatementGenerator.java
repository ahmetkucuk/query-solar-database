package hek.core;

import hek.models.Event;
import hek.utils.Constants;

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
            String attributeValue = event.getAttr(attribute);
            if(attributeValue == null || attributeValue.isEmpty()) {
                return null;
            }
            return String.format(Constants.QUERY.GEOMETRY_FORM, event.getAttr(attribute));
        }


        if(attribute.equalsIgnoreCase("event_starttime")) {
            return "'" + (event.getAttr(attribute) != null ? event.getAttr(attribute).replaceAll("'", " ") : "") + "'";
        } else if(attribute.equalsIgnoreCase("event_endtime")) {
            if(event.getAttr("event_endtime").equalsIgnoreCase(event.getAttr("event_starttime"))) {
                return "TIMESTAMP '" + (event.getAttr(attribute) != null ? event.getAttr(attribute).replaceAll("'", " ") : "") + "' + INTERVAL '1 second'";
            }
            return "'" + (event.getAttr(attribute) != null ? event.getAttr(attribute).replaceAll("'", " ") : "") + "'";
        }

        if(!attributeDataTypeMap.containsKey(attribute) || attributeDataTypeMap.get(attribute).equalsIgnoreCase("string")) {
            return "'" + (event.getAttr(attribute) != null ? event.getAttr(attribute).replaceAll("'", " ") : "") + "'";
        }

        String attributeValue = event.getAttr(attribute);
        if(attributeValue == null || event.getAttr(attribute).length() == 0) return "0";
        return event.getAttr(attribute);
    }

    public List<String> getInsertQueries() {
        return insertQueries;
    }
}
