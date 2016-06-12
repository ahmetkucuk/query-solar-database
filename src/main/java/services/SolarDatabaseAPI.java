package services;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import core.CreateTableStatementGenerator;
import core.DBConnection;
import core.EventJsonDownloader;
import core.InsertStatementGenerator;
import models.Event;
import models.EventType;
import org.apache.commons.lang3.StringUtils;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by ahmetkucuk on 28/12/15.
 */
public class SolarDatabaseAPI {

    private static final int numberOfElementInEachQuery = 200;

    public void downloadAndInsertEvents(String startDate, String endDate) {

        String eventsToBeIncluded = StringUtils.join(EventType.getARSGFLCH().stream().map(e -> e.toQualifiedString()).collect(Collectors.toList()), ",");
        EventJsonDownloader eventJsonDownloader = new EventJsonDownloader(eventsToBeIncluded, startDate, endDate, numberOfElementInEachQuery);

        JsonArray array;
        Set<String> insertedEvents = new HashSet<>();
        while ((array = eventJsonDownloader.next()) != null) {
            for (JsonElement j : array) {
                Event e = new Event(j.getAsJsonObject());
                String kb = e.get("kb_archivid");
                if (!insertedEvents.contains(kb)) {
                    new InsertStatementGenerator(e).getInsertQueries().forEach(q -> DBConnection.getInstance().executeCommand(q));
                    insertedEvents.add(kb);
                }
            }
        }
    }

    public void createDatabaseSchema() {

        new CreateTableStatementGenerator().createTables();
    }

//    public void addAdditionalFunctions() {
//
//        String[] listOfFunctions = new String[]{"query/temporal_filter.sql"};
//        for(String fileName : listOfFunctions) {
//            DBConnection.getInstance().executeFromFile(FileManager.getInstance().getPath(fileName));
//        }
//    }
}
