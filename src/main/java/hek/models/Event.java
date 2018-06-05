package hek.models;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * Class for Solar Event
 * Created by ahmetkucuk on 01/10/15.
 * @author ahmetkucuk
 * @author jookimmy
 */
public class Event {

    /**
     * Variable for Event Type Class.
     */
    private EventType eventType;

    /**
     * Variable in JsonObject class that gives information on the event.
     */
    private JsonObject eventJson;

    /**
     * Event class constructor.
     * @param j - event input in JSONObject format
     */
    public Event(JsonObject j) {
        this.eventJson = j;
    }

    /**
     * Event class constructor.
     * @param j - event input in JSONObject format
     * @param e - event type input
     */
    public Event(JsonObject j, EventType e) {
        this.eventJson = j;
        this.eventType = e;
    }

    /**
     * Retrieves attribute from eventJson as element and returns as String.
     * @param attr - attribute input to use for get method
     * @return String
     */
    public String getAttr(String attr) {
        //If eventJson does not have requested attribute
        if(!eventJson.has(attr)) return null;

        //Cut primary key
//        if(attr.equalsIgnoreCase("kb_archivid")) {
//            String kbArchivId = eventJson.get(attr).getAsString();
//            return kbArchivId.substring(kbArchivId.lastIndexOf("/")+1);
//        }

        //Retrieving jsonElement attribute from eventJson
        JsonElement jsonElement = eventJson.get(attr);
        if(jsonElement != null && jsonElement.isJsonArray()) {
            //Return jsonElement as String
            return jsonElement.toString();
        }
        return jsonElement != null && !jsonElement.isJsonNull() ? jsonElement.getAsString() : null;
    }

    /**
     * Method returning Event Type applying the getAttr function.
     * @return eventType
     */
    public EventType getEventType() {
        if(eventType == null) {
            //Implements getAttr method to assign an element to EventType class.
            eventType = EventType.fromString(getAttr("event_type"));
        }
        return eventType;
    }

    /**
     * Void method to set event type in class instance.
     * @param eventType - input to set type with.
     */
    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

}
