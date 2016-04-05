package models;

import java.util.Date;

/**
 * Created by ahmetkucuk on 21/02/16.
 */
public class ImageAttributes {

    private static final String INSERT_INTO_IMAGE_ARRTIBUTES = "INSERT INTO %s (date, measurement, observatory, instrument, name) VALUES (%s);";
    private static final String CREATE_TABLE = "DROP TABLE IF EXISTS %s;CREATE TABLE %s (date TIMESTAMP, measurement text, observatory text, instrument text, name text PRIMARY KEY);";
    //select ('2012-07-06 09:51:07'::timestamp - date) as absolute from image_attributes WHERE measurement like '171' ORDER BY absolute DESC;

    //2012_06_04__09_41_43_84__SDO_AIA_AIA_193.jp2
    private Date date;
    private String measurement;
    private String observatory;
    private String instrument;
    private String completeName;
    private String tableName;

    public ImageAttributes(Date date, String measurement, String observatory, String instrument, String completeName, String tableName) {
        this.date = date;
        this.measurement = measurement;
        this.observatory = observatory;
        this.instrument = instrument;
        this.completeName = completeName;
        this.tableName = tableName;
    }

    public String getInsertQuery() {
        return String.format(INSERT_INTO_IMAGE_ARRTIBUTES, tableName, ("'" + date + "', '" + measurement + "', '" + observatory + "', '" + instrument + "', '" + completeName + "'"));
    }

    public static String getCreateTableQuery(String tableName) {
        return String.format(CREATE_TABLE, tableName, tableName);
    }

    public String getCreateTableQuery() {
        return String.format(CREATE_TABLE, tableName, tableName);
    }

    @Override
    public String toString() {
        return "ImageAttributes{" +
                "date=" + date +
                ", measurement='" + measurement + '\'' +
                ", observatory='" + observatory + '\'' +
                ", instrument='" + instrument + '\'' +
                ", completeName='" + completeName + '\'' +
                '}';
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getMeasurement() {
        return measurement;
    }

    public void setMeasurement(String measurement) {
        this.measurement = measurement;
    }

    public String getObservatory() {
        return observatory;
    }

    public void setObservatory(String observatory) {
        this.observatory = observatory;
    }

    public String getInstrument() {
        return instrument;
    }

    public void setInstrument(String instrument) {
        this.instrument = instrument;
    }

    public String getCompleteName() {
        return completeName;
    }

    public void setCompleteName(String completeName) {
        this.completeName = completeName;
    }
}
