package hek.core;

import hek.models.EventType;
import hek.utils.EventAttributeSeparator;
import hek.utils.FileManager;
import hek.utils.Utilities;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by ahmetkucuk on 5/22/17.
 */
public class EventAttributeManager {

    private static EventAttributeManager instance;
    private Map<EventType, Set<String>> eventTypeByAttributes;
    private Map<String, String> attributeDataTypeMap;

    public static final Set<String> geometryAttributes = new HashSet<>();
    static {
        geometryAttributes.add("hgc_bbox");
        geometryAttributes.add("hgc_coord");
        geometryAttributes.add("hgs_bbox");
        geometryAttributes.add("hgs_coord");
        geometryAttributes.add("hpc_bbox");
        geometryAttributes.add("hpc_coord");
        geometryAttributes.add("hrc_bbox");
        geometryAttributes.add("hrc_coord");

        geometryAttributes.add("hgc_boundcc");

        geometryAttributes.add("hgc_boundcc");
        geometryAttributes.add("hgs_boundcc");
        geometryAttributes.add("hpc_boundcc");
        geometryAttributes.add("hrc_boundcc");
        geometryAttributes.add("bound_chaincode");

        geometryAttributes.add("hrc_skeletoncc");
        geometryAttributes.add("hgc_skeletoncc");
        geometryAttributes.add("hgs_skeletoncc");
        geometryAttributes.add("hpc_skeletoncc");
    }

    private EventAttributeManager(){}

    public static void init() {
        instance = new EventAttributeManager();
        instance.setEventTypeByAttributes(EventAttributeSeparator.getEventTypeByAttributesMap());
        instance.setAttributeDataTypeMap(Utilities.getAttributesMap(FileManager.getInstance().getInputStream("/data/designv1/Parameter_Types.txt")));
    }

    public static EventAttributeManager getInstance() {
        if(instance == null)
            System.err.println("Not initialized");
        return instance;
    }

    public Map<EventType, Set<String>> getEventTypeByAttributes() {
        return eventTypeByAttributes;
    }

    public void setEventTypeByAttributes(Map<EventType, Set<String>> eventTypeByAttributes) {
        this.eventTypeByAttributes = eventTypeByAttributes;
    }

    public Map<String, String> getAttributeDataTypeMap() {
        return attributeDataTypeMap;
    }

    public void setAttributeDataTypeMap(Map<String, String> attributeDataTypeMap) {
        this.attributeDataTypeMap = attributeDataTypeMap;
    }
}
