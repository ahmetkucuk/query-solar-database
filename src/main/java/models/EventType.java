package models;

/**
 * Created by ahmetkucuk on 01/10/15.
 */
public enum EventType {
    GE, AR, CH, FL, SG;

    public static EventType fromString(String s) {
        switch (s) {
            case "GE":
                return GE;
            case "AR":
                return AR;
            case "CH":
                return CH;
            case "FL":
                return FL;
            case "SG":
                return SG;
        }
        return null;
    }
}
