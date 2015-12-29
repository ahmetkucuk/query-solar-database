
//import com.sun.deploy.util.StringUtils;
import com.google.common.collect.Sets;

import core.*;
import models.DBTable;
import services.SolarDatabaseAPI;
import utils.FileManager;
import utils.Utilities;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;


/**
 * Created by ahmetkucuk on 18/11/15.
 */
public class Runner {

    private static final String START_DATE = "2015-12-01T00:00:00";
    private static final String END_DATE = "2015-12-05T23:00:59";

    public static void main(String[] args) {
        new DBPrefs();
    	
        GlobalAttributeHolder.init();
        SolarDatabaseAPI api = new SolarDatabaseAPI();
        api.createDatabaseSchema();
        api.downloadAndInsertEvents(START_DATE, END_DATE);

    }


}
