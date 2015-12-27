package core;

import com.google.gson.JsonObject;
import models.Event;
import utils.Constants;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by ahmetkucuk on 26/12/15.
 */
public class InsertQueryForEvent {

    private Event event;
    private String insertQuery;

    public InsertQueryForEvent(Event e) {
        this.event = e;
        generateInsertQuery();
    }

    private void generateInsertQuery() {

        List<String> values = new LinkedList<>();
        Set<String> attributes = GlobalAttributeHolder.getInstance().getEventTypeByAttributes().get(event.getEventType());
        values.addAll(attributes.stream().map(attribute -> getActualPostgreMapping(event, attribute)).collect(Collectors.toList()));
        insertQuery = String.format(Constants.QUERY.INSERT_INTO, event.getEventType().toString(), String.join(",", attributes), String.join(",", values));
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

        if(!attributeDataTypeMap.containsKey(attribute)) {
            return "'" + event.get(attribute) + "'";
        } else if(attributeDataTypeMap.get(attribute).equalsIgnoreCase("string")) {
            return "'" + event.get(attribute) + "'";
        } else if(attributeDataTypeMap.get(attribute).equalsIgnoreCase("timestamp")) {
            return "'" + event.get(attribute) + "'";
        }

        String attributeValue = event.get(attribute);
        if(attributeValue == null || event.get(attribute).length() == 0) return "0";
        return event.get(attribute);
    }

    public String getInsertQuery() {
        return insertQuery;
    }
}
