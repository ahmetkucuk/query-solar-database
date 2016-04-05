package temp;

/**
 * Created by ahmetkucuk on 01/10/15.
 */
public enum EventType {
    AR, CH, FL, SG;

    public static EventType fromString(String s) {
        switch (s) {
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

    public int getMeasurement() {
        switch (this) {
            case AR:
                return Constants.Measurement.AR_ME;
            case CH:
                return Constants.Measurement.CH_ME;
            case FL:
                return Constants.Measurement.FL_ME;
            case SG:
                return Constants.Measurement.SG_ME;
        }
        return 0;
    }

    public int getSecondaryMeasurement() {
        switch (this) {
            case AR:
                return Constants.Measurement.S_AR_ME;
            case CH:
                return Constants.Measurement.S_CH_ME;
            case FL:
                return Constants.Measurement.S_FL_ME;
            case SG:
                return Constants.Measurement.S_SG_ME;
        }
        return 0;
    }
}
