package utils;

/**
 * Created by ahmetkucuk on 27/09/15.
 */
public class Constants {

    public static class FieldNames {

        public static final String START_DATE = "event_starttime";
        public static final String END_DATE = "event_endtime";
        public static final String CHANNEL_ID = "obs_channelid";
        public static final String POLYGON = "hpc_bbox";
        public static final String EVENT_TYPE = "event_type";
    }

    public static class DB {
        public static final String DB_HOST = "localhost";
        public static final String DB_NAME = "ahmetkucuk";
        public static final String DB_USERNAME = "ahmetkucuk";
        public static final String DB_USER_PASSWORD = "";
        public static final int DB_PORT = 0;
    }

    public static class QUERY {
        public static final String[] AR_ATTRIBUTE_LIST = new String[] {"event_starttime", "kb_archivid", "event_type"};
        public static final String INSERT_INTO_AR = "INSERT INTO AR (" + String.join(",", AR_ATTRIBUTE_LIST) + ") VALUES (%s);";
    }
}
