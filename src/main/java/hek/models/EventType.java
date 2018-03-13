package hek.models;

import java.util.Arrays;
import java.util.List;

/**
 * Created by ahmetkucuk on 01/10/15.
 */
public enum EventType {
    AR, CE, CD, CH, CW, FI, FE, FA, FL, LP, OS, SS, EF, CJ, PG, OT, NR, SG, SP, CR, CC, ER, TOB, HY, BU, EE, PB, PT;


    public static EventType fromString(String s) {
        if(s.equalsIgnoreCase("TO")) return TOB;
        for(EventType eventType: EventType.values()) {
            if(eventType.toString().equalsIgnoreCase(s)) return eventType;
        }
        return null;
    }

    public String toQualifiedString() {
        if(this == TOB) return "TO";
        return super.toString();
    }

    public static List<EventType> getARSGFLCH() {
        return Arrays.asList(AR, SG, FL, CH);
    }

    public static List<EventType> getAsList() {
        return Arrays.asList(AR, CE, CD, CH, CW, FI, FE, FA, FL, LP, OS, SS, EF, CJ, PG, OT, NR, SG, SP, CR, CC, ER, TOB, HY);
    }
}
