package hek.utils;

import hek.models.DBTable;
import hek.models.EventType;

import java.io.*;
import java.util.*;

/**
 * Created by ahmetkucuk on 26/12/15.
 */
public class EventAttributeSeparator {

    public static Map<EventType, Set<String>> getEventTypeByAttributesMap() {

        try(BufferedReader reader = new BufferedReader(new InputStreamReader(FileManager.getInstance().getInputStream("/data/designv3/attributes.tsv")))) {

            Map<EventType, Set<String>> eventTypeByAttributes = new TreeMap<>();
            String[][] all = new String[241][31];
            String line ;
            int count = 0;
            while((line = reader.readLine()) != null) {
                String[] values = line.split("\t");
                all[count] = values;
                count++;
            }

            for(int i = 2; i < all[0].length - 1; i++) {
                Set<String> attributesSpecificToEvent = new HashSet<>();
                for(int k = 1; k < all.length; k++) {
                    if(all[k][i].equalsIgnoreCase("opt") || all[k][i].equalsIgnoreCase("req")) {
                        attributesSpecificToEvent.add(all[k][0].toLowerCase());
                    }
                }
                eventTypeByAttributes.put(EventType.fromString(all[0][i]), attributesSpecificToEvent);
            }
            return eventTypeByAttributes;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
