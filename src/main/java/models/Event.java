package models;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
/**
 * Created by ahmetkucuk on 01/10/15.
 */
public class Event {

    private EventType eventType;
    private JsonObject eventJson;

    public Event(JsonObject j) {
        this.eventJson = j;
    }

    public String get(String attr) {
        JsonElement jsonElement = eventJson.get(attr);
        return jsonElement != null ? jsonElement.getAsString() : null;
    }
}
