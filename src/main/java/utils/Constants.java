package utils;

/**
 * Created by ahmetkucuk on 27/09/15.
 */
public class Constants {

    public static class DATA {

        public static final int NUMBER_OF_DATA_FROM_EACH_FILE = 10000;
        public static final String EVENT_ATTRIBUTE_LIST_BASE = "data/%sprivate.txt";
        public static final String ATTRIBUTE_LIST = "data/old-design/Parameter_Types.txt";
    }

    public static class FieldNames {

        public static final String START_DATE = "event_starttime";
        public static final String END_DATE = "event_endtime";
        public static final String CHANNEL_ID = "obs_channelid";
        public static final String POLYGON = "hpc_bbox";
        public static final String EVENT_TYPE = "event_type";
    }

    public static class DB {
        public static final String DB_HOST = "localhost";
        public static final String DB_NAME = "dbproject";
        public static final String DB_USERNAME = "ahmetkucuk";
        public static final String DB_USER_PASSWORD = "";
        public static final int DB_PORT = 0;
    }

    public static class QUERY {

        public static final String INSERT_INTO = "INSERT INTO %s (%s) VALUES (%s);";
        public static final String GEOMETRY_FORM = "ST_GeomFromText('%s',4326)";
    }
}
