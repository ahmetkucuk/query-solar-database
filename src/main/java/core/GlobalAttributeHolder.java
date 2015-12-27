package core;

import models.DBTables;
import models.EventType;
import utils.EventAttributeSeparator;
import utils.FileManager;
import utils.Utilities;

import java.util.List;
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

    private GlobalAttributeHolder(){}

    public static void init() {
        instance = new GlobalAttributeHolder();
        instance.setEventTypeByAttributes(EventAttributeSeparator.getEventTypeByAttributesMap());
        instance.setAdditionalAttributes(Utilities.fileAsSet(FileManager.getInstance().getPath("/data/newdesign/additional")));
        instance.setDbTablesByAttributes(EventAttributeSeparator.getDbTablesByAttributesMap());
        instance.addAdditionalValues();
    }

    public static GlobalAttributeHolder getInstance() {
        if(instance == null)
            System.err.println("Not initialized");
        return instance;
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

    private void addAdditionalValues() {
        for(Set<String> s: eventTypeByAttributes.values()) {
            s.addAll(additionalAttributes);
        }
    }
}
