package models;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.sun.tools.javac.file.SymbolArchive;

/**
 * Created by ahmetkucuk on 01/10/15.
 */
public class Event {

    private EventType eventType;
    private JsonObject eventJson;

    public Event(JsonObject j) {
        this.eventJson = j;
    }

    public Event(JsonObject j, EventType e) {
        this.eventJson = j;
        this.eventType = e;
    }

    public String get(String attr) {

        if(!eventJson.has(attr)) return null;

        //Cut primary key
//        if(attr.equalsIgnoreCase("kb_archivid")) {
//            String kbArchivId = eventJson.get(attr).getAsString();
//            return kbArchivId.substring(kbArchivId.lastIndexOf("/")+1);
//        }
        JsonElement jsonElement = eventJson.get(attr);
        if(jsonElement != null && jsonElement.isJsonArray()) {
            return jsonElement.toString();
        }
        return jsonElement != null && !jsonElement.isJsonNull() ? jsonElement.getAsString() : null;
    }

    public EventType getEventType() {
        if(eventType == null) {
            eventType = EventType.fromString(get("event_type"));
        }
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

}
