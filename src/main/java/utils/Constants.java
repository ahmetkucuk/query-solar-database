package utils;

/**
 * Created by ahmetkucuk on 27/09/15.
 */
public class Constants {

	public static final String SDO_RELEASE_DATE = "2010-01-01T00:00:00";

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

    public static class QUERY {

        public static final String INSERT_INTO = "INSERT INTO %s (%s) VALUES (%s);";
        public static final String GEOMETRY_FORM = "ST_GeomFromText('%s',4326)";
        public static final String ADD_GEOMETRY_COLUMN = "SELECT AddGeometryColumn('%s', '%s',4326,'%s',2);";
    }
    
    public static class TRIGGER {
    	public static final String CREATE_ST_TABLE = 
    			" DROP TABLE IF EXISTS %s_spt; \n"
    			+ "CREATE TABLE %s_spt(" +
    			"  kb_archivid text NOT NULL, " +
    			"  event_starttime timestamp without time zone, " +
    			"  event_endtime timestamp without time zone, " +
    			"  hpc_bbox geometry(Polygon,4326), " +
    			"  hpc_boundcc geometry(Polygon,4326), " +
    			"  trajectory_id bigint," +
    			"  interpolated boolean," +
    			"  intrajectory boolean, " + 
    			" CONSTRAINT %s_spt_pkey PRIMARY KEY (kb_archivid)" +
    			");" + "\n"
    			+ "CREATE INDEX"
    			+ " ON %s_spt USING btree ( tsrange(event_starttime, event_endtime, \'[]\') );\n"
    			+ "CREATE INDEX %s_spt_hpc_bbox_idx"
    			+ " ON %s_spt USING gist (hpc_bbox);"; 
   
    			
    			
    	public static final String CREATE_TRIGGER = 
    			"CREATE OR REPLACE FUNCTION %s_ins_function() RETURNS trigger AS \' \n" +
    			"	BEGIN	\n" +
				"	     INSERT INTO %s_spt(kb_archivid, event_starttime, event_endtime, hpc_bbox, hpc_boundcc, interpolated) \n" +
				"		  VALUES (new.kb_archivid, new.event_starttime, new.event_endtime, new.hpc_bbox, new.hpc_boundcc, FALSE); \n" +
				"			RETURN new; \n" +
				"	END \n"  +
				"\' LANGUAGE plpgsql; \n" +
				"DROP TRIGGER IF EXISTS %s_ins on %s; \n" +
				" CREATE TRIGGER %s_ins AFTER INSERT -- OR DELETE OR UPDATE \n" +
				"        ON %s FOR each ROW \n " +
				"        EXECUTE PROCEDURE %s_ins_function(); \n";
    }
}
