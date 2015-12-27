package utils;

import models.EventType;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by ahmetkucuk on 26/12/15.
 */
public class EventAttributeSeparator {

    public static final String allEvents = "/Users/ahmetkucuk/Documents/Research/SolarDatabase/EventAttributes/attributes.tsv";

    public static void main(String[] args) {

        try(BufferedReader reader = new BufferedReader(new FileReader(allEvents))) {

            Map<EventType, List<String>> eventTypeByAttributes = new TreeMap<>();
            String[][] all = new String[197][27];
            String line ;
            int count = 0;
            while((line = reader.readLine()) != null) {
                String[] values = line.split("\t");
                all[count] = values;
                count++;
            }

            for(int i = 2; i < all[0].length - 1; i++) {
                List<String> attributesSpecificToEvent = new ArrayList<>();
                for(int k = 1; k < all.length; k++) {
                    if(all[k][i].equalsIgnoreCase("opt") || all[k][i].equalsIgnoreCase("req")) {
                        attributesSpecificToEvent.add(all[k][0]);
                    }
                }
                eventTypeByAttributes.put(EventType.fromString(all[0][i]), attributesSpecificToEvent);
            }
            System.out.println(eventTypeByAttributes);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class Pair {
        public String key;
        public String value;
    }
}
