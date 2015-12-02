package core;

import models.Event;
import models.EventType;
import utils.Constants;
import utils.FileManager;
import utils.Utilities;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by ahmetkucuk on 24/11/15.
 */
public class InsertTableQueries {

    private List<Event> events;
    private List<String> insertQueries = new LinkedList<>();
    private List<String> insertGEQueries = new LinkedList<>();
    private Map<String, String> attributeDataTypeMap;
    private Map<EventType, List<String>> eventTypeByAttributeMap;

    private static final Set<String> geometryAttribute = new HashSet<>();
    static {
        geometryAttribute.add("hgc_bbox");
        geometryAttribute.add("hgc_coord");
        geometryAttribute.add("hgs_bbox");
        geometryAttribute.add("hgs_coord");
        geometryAttribute.add("hpc_bbox");
        geometryAttribute.add("hpc_coord");
        geometryAttribute.add("hrc_bbox");
        geometryAttribute.add("hrc_coord");

        geometryAttribute.add("hgc_boundcc");

        geometryAttribute.add("hgc_boundcc");
        geometryAttribute.add("hgs_boundcc");
        geometryAttribute.add("hpc_boundcc");
        geometryAttribute.add("hrc_boundcc");
        geometryAttribute.add("bound_chaincode");
    }



    public InsertTableQueries(String fileName) {
        initAttributeMap();
        events = Utilities.convertFileToJSON(fileName, Constants.DATA.NUMBER_OF_DATA_FROM_EACH_FILE);
        insertQueries.addAll(events.stream().map(event -> generateInsertQuery(event)).collect(Collectors.toList()));
        insertGEQueries.addAll(events.stream().map(event -> generateGEInsertQuery(event)).collect(Collectors.toList()));
    }

    public String generateInsertQuery(Event event) {

        List<String> values = new LinkedList<>();
        List<String> attributes = eventTypeByAttributeMap.get(event.getEventType());
        values.addAll(attributes.stream().map(attribute -> getActualPostgreMapping(event, attribute)).collect(Collectors.toList()));
        return String.format(Constants.QUERY.INSERT_INTO, event.getEventType().toString(), String.join(",", attributes), String.join(",", values));
    }

    public String generateGEInsertQuery(Event event) {

        List<String> values = new LinkedList<>();
        List<String> attributes = eventTypeByAttributeMap.get(EventType.GE);
        values.addAll(attributes.stream().map(attribute -> getActualPostgreMapping(event, attribute)).collect(Collectors.toList()));
        return String.format(Constants.QUERY.INSERT_INTO, EventType.GE.toString(), String.join(",", attributes), String.join(",", values));
    }

    public String getActualPostgreMapping(Event event, String attribute) {

        if(geometryAttribute.contains(attribute.toLowerCase())) {
            String attributeValue = event.get(attribute);
            if(attributeValue == null || attributeValue.isEmpty()) {
                return null;
            }
            return String.format(Constants.QUERY.GEOMETRY_FORM, event.get(attribute));
        }

        if(!attributeDataTypeMap.containsKey(attribute)) {
            return "'" + event.get(attribute) + "'";
        }

        if(attributeDataTypeMap.containsKey(attribute) && attributeDataTypeMap.get(attribute).equalsIgnoreCase("string")) {
            return "'" + event.get(attribute) + "'";
        }
        String attributeValue = event.get(attribute);
        if(attributeValue == null || event.get(attribute).length() == 0) return "0";
        return event.get(attribute);
    }

    private void initAttributeMap() {

        attributeDataTypeMap = Utilities.getAttributesMap(FileManager.getInstance().getPath(Constants.DATA.ATTRIBUTE_LIST));

        eventTypeByAttributeMap = new HashMap<>();
        for(EventType e: EventType.values()) {
            String fileName = String.format(Constants.DATA.EVENT_ATTRIBUTE_LIST_BASE, e.toString());
            fileName = FileManager.getInstance().getPath(fileName);
            eventTypeByAttributeMap.put(e, Utilities.getPrivateAttrSet(fileName));
        }
    }

    public List<String> getInsertQueries() {return insertQueries;}
    public List<String> getInsertGEQueries() {return insertGEQueries;}

}
