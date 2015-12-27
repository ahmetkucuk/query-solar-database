package core;

import models.DBTables;
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
    private Map<DBTables, Set<String>> dbTablesByAttributes;
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
//        System.out.println(FileManager.getInstance().getPath("data/newdesign/SpAttr_table"));
        instance.setAdditionalAttributes(Utilities.fileAsSet(FileManager.getInstance().getPath("data/newdesign/SpAttr_table")));
        instance.setDbTablesByAttributes(EventAttributeSeparator.getDbTablesByAttributesMap());
        instance.setAttributeDataTypeMap(Utilities.getAttributesMap(FileManager.getInstance().getPath("data/old-design/Parameter_Types.txt")));
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

    private void setDbTablesByAttributes(Map<DBTables, Set<String>> dbTablesByAttributes) {
        this.dbTablesByAttributes = dbTablesByAttributes;
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

    public Map<DBTables, Set<String>> getDbTablesByAttributes() {
        return dbTablesByAttributes;
    }

    public Set<String> getAdditionalAttributes() {
        return additionalAttributes;
    }


    public static Set<String> getGeometryAttribute() {
        return geometryAttribute;
    }
}
