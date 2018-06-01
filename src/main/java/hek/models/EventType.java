package hek.models;

import java.util.Arrays;
import java.util.List;

/**
 * Created by ahmetkucuk on 01/10/15.
 * @author ahmetkucuk
 * @author jookimmy
 */
public enum EventType {
    /**
     * Acronyms representing all the different types of solar events. 
     * Use "http://www.lmsal.com/hek/VOEvent_Spec.html" for specifics.
     */
    AR, CE, CD, CH, CW, FI, FE, FA, FL, LP, OS, SS, EF, CJ, PG, OT, NR, SG, SP, CR, CC, ER, TOB, HY, BU, EE, PB, PT;

    /**
     * Uses String representation to wrap into the EventType class object.
     * @param s - string representation
     * @return EventType object
     */
    public static EventType fromString(String s) {
        if(s.equalsIgnoreCase("TO")) return TOB;
        for(EventType eventType: EventType.values()) {
            if(eventType.toString().equalsIgnoreCase(s)) return eventType;
        }
        return null;
    }

    /**
     * A toString variation which has a condition for the EventType "TOB".
     * @return String representation for EventType object
     */
    public String toQualifiedString() {
        if(this == TOB) return "TO";
        return super.toString();
    }

    /**
     * This returns List of Solar Event Types (not events)
     * @return list of Solar Event Types
     */
    public static List<EventType> getAsList() {
        return Arrays.asList(AR, CE, CD, CH, CW, FI, FE, FA, FL, LP, OS, SS, EF, CJ, PG, OT, NR, SG, SP, CR, CC, ER, TOB, HY);
    }
}
