import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import org.apache.commons.lang3.StringUtils;
//import com.sun.deploy.util.StringUtils;
import core.*;
import models.Event;
import models.EventType;
import services.SolarDatabaseAPI;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * Created by ahmetkucuk on 18/11/15.
 */
public class Runner {

    private static final String START_DATE = "2015-12-01T00:00:00";
    private static final String END_DATE = "2015-12-03T23:00:59";

    public static void main(String[] args) {

        GlobalAttributeHolder.init();
        SolarDatabaseAPI api = new SolarDatabaseAPI();
        api.createDatabaseSchema();
        api.downloadAndInsertEvents(START_DATE, END_DATE);
    }


}
