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

    public static class CDELT {
        public static final double AR_CDELT = 0.599733;
        public static final double CH_CDELT = 0.599733;
        public static final double FL_CDELT = 0.599733;
        public static final double SG_CDELT = 0.599733;
    }

    public static class Measurement {
        public static final int AR_ME = 171;
        public static final int CH_ME = 193;
        public static final int FL_ME = 193;
        public static final int SG_ME = 94;


        public static final int S_AR_ME = 193;
        public static final int S_CH_ME = 171;
        public static final int S_FL_ME = 131;
        public static final int S_SG_ME = 131;
    }

    public static final String INPUT_FILE_NAME_META = "/Users/ahmetkucuk/Documents/Research/DNNProject/formattedop/%s/RECORD/%s_Records.txt";
    public static final String INPUT_FILE_EXTRACTED_NAME_META = "/Users/ahmetkucuk/Documents/Research/DNNProject/formattedop/%s/RECORD/%s_Records.txt-Extracted-Attributes.txt";
    public static final String INPUT_FILE_NAME_BAD_META = "/Users/ahmetkucuk/Documents/Research/DNNProject/formattedop/%s/BAD/%s_Bad_Records.txt";


    public static final String OUTPUT_FILE_NAME_META = "/Users/ahmetkucuk/Documents/Developer/java/QueryHEK/Image/%s/";

    public static final String JPEG_IMAGE_FILE_LOCATION_META = "/Users/ahmetkucuk/Documents/Developer/java/QueryHEK/Image/%s/jpegs/";



    public static final String IMAGE_DOWNLOAD_URL = "http://gs671-suske.ndc.nasa.gov/api/v1/getJP2Image/?date=%s&instrument=AIA&observatory=SDO&detector=AIA&measurement=%s";
    public static final String IMAGE_JPIP_URI_DOWNLOAD_URL = "http://gs671-suske.ndc.nasa.gov/api/v1/getJP2Image/?date=%s&instrument=AIA&observatory=SDO&detector=AIA&measurement=%s&jpip=true";

    public static final String IMAGE_DIRECTORY = "/Users/ahmetkucuk/Documents/Developer/java/QueryHEK/Image/";

    String convertImages = "sips -s format jpeg ./*.jp2 --out jpegs";
}
