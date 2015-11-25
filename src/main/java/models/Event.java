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
        JsonElement jsonElement = eventJson.get(attr);
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
