package core;

import models.DBTable;
import models.EventType;
import utils.EventAttributeSeparator;
import utils.FileManager;
import utils.Utilities;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by ahmetkucuk on 26/12/15.
 */
public class GlobalAttributeHolder {

    private static GlobalAttributeHolder instance;

    private Map<EventType, Set<String>> eventTypeByAttributes;
    private Map<DBTable, Set<String>> dbTableByAttributes;
    private Set<String> additionalAttributes;
    private Map<String, String> attributeDataTypeMap;

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

        geometryAttribute.add("hrc_skeletoncc");
        geometryAttribute.add("hgc_skeletoncc");
        geometryAttribute.add("hgs_skeletoncc");
        geometryAttribute.add("hpc_skeletoncc");
    }

    private GlobalAttributeHolder(){}

    public static void init() {
        instance = new GlobalAttributeHolder();
        instance.setEventTypeByAttributes(EventAttributeSeparator.getEventTypeByAttributesMap());

        instance.setAdditionalAttributes(Utilities.fileAsSet(FileManager.getInstance().getInputStream("/data/designv2/additional_attributes")));
        instance.setAttributeDataTypeMap(Utilities.getAttributesMap(FileManager.getInstance().getInputStream("/data/designv1/Parameter_Types.txt")));

        instance.setDbTableByAttributes(EventAttributeSeparator.getDbTablesByAttributesMap());
        instance.addAdditionalValues();
    }

    public static GlobalAttributeHolder getInstance() {
        if(instance == null)
            System.err.println("Not initialized");
        return instance;
    }

    private void addAdditionalValues() {
        for(Set<String> s: eventTypeByAttributes.values()) {
            s.addAll(additionalAttributes);
        }
    }

    private void setEventTypeByAttributes(Map<EventType, Set<String>> m) {
        this.eventTypeByAttributes = m;
    }

    private void setAdditionalAttributes(Set<String> additionalAttributes) {
        this.additionalAttributes = additionalAttributes;
    }

    public Map<DBTable, Set<String>> getDbTableByAttributes() {
        return dbTableByAttributes;
    }

    private void setDbTableByAttributes(Map<DBTable, Set<String>> dbTableByAttributes) {
        this.dbTableByAttributes = dbTableByAttributes;
    }

    private static void setInstance(GlobalAttributeHolder instance) {
        GlobalAttributeHolder.instance = instance;
    }

    public Map<String, String> getAttributeDataTypeMap() {
        return attributeDataTypeMap;
    }

    public void setAttributeDataTypeMap(Map<String, String> attributeDataTypeMap) {
        this.attributeDataTypeMap = attributeDataTypeMap;
    }

    public Map<EventType, Set<String>> getEventTypeByAttributes() {
        return eventTypeByAttributes;
    }

    public Set<String> getAdditionalAttributes() {
        return additionalAttributes;
    }


    public Set<String> getGeometryAttribute() {
        return geometryAttribute;
    }
}
