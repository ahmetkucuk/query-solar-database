package hek.core;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import hek.utils.StatusLogger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Class that downloads events from the HEK api
 * @author - ahmetkucuk
 * @author - kqian5
 */
public class EventJsonDownloader implements Comparable<EventJsonDownloader>{

    private String eventType;
    private String eventStartTime;
    private String eventEndTime;
    private int resultLimit;
    private int page = 1;
    private boolean isFinished = false;

    /**
     * HEK api that includes formatting for specific event type, page,
     * event start time, and event end time
     */
    private static final String URL = "https://www.lmsal.com/hek/her?cosec=2&cmd=search&type=column&event_type=%s&event_region=all&event_coordsys=helioprojective&x1=-5000&x2=5000&y1=-5000&y2=5000&result_limit=%d&page=%d&event_starttime=%s&event_endtime=%s";

    /**
     * Constructor for an EventJsonDownloader object
     * @param eventType - type of the event
     * @param eventStartTime - start time of the event
     * @param eventEndTime - end time of the event
     * @param resultLimit - limit for
     * @param page - page to start from
     */
    public EventJsonDownloader(String eventType, String eventStartTime, String eventEndTime, int resultLimit, int page) {
        this.eventType = eventType;
        this.eventStartTime = eventStartTime;
        this.eventEndTime = eventEndTime;
        this.resultLimit = resultLimit;
        this.page = page;
    }

    /**
     * Method that downloads the event data from the next page
     * of the HEK api
     * @return - the event data as a json array
     * @throws Exception
     */
    public JsonArray next() throws Exception{
        if(isFinished) return null;
        String url = getNextUrl();
        String downloaded = downloadByUrl(url);
        if(downloaded == null) return new JsonArray();
        StatusLogger.getInstance().writeLatestDownloaded(eventStartTime + "SEP" + eventEndTime + "SEP" + page);
        return getResultArray(downloaded);
    }

    /**
     * Method that increments the page by 1 and reformats the 
     * url for the HEK api
     * @return - the reformatted url
     */
    private String getNextUrl() {
        String result = String.format(URL, eventType, resultLimit, page, eventStartTime, eventEndTime);
        page++;
        return result;
    }

    /**
     * Method that extracts all of the data from the HEK api and returns it as a 
     * string 
     * @param url - the HEK api url
     * @return - the data from the api
     * @throws IOException - input/output exception caused from reading from url
     */
    private String downloadByUrl(String url) throws IOException {

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new URL(url).openStream()))) {
            StringBuilder sb = new StringBuilder();

            String inputLine;

            //Build a string from streamed data
            while ((inputLine = reader.readLine()) != null) {
                sb.append(inputLine + "\n");
            }
            return sb.toString();
        } catch (MalformedURLException e) {
            System.out.println("[EventJsonDownloader-downloadByUrl] MalformedURLException");
        }
        return null;
    }

    /**
     * Method that takes a json formatted string and turns it 
     * into a json object. The "result" attribute is then looked 
     * up from the json object and returned as a json array.
     * @param result - the json formatted string from the HEK api
     * @return - the data of the "result" attribute from the json
     * containing information about the event
     */
    private JsonArray getResultArray(String result) {
        JsonParser parser = new JsonParser();
        JsonElement jsonElement = parser.parse(result);
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        isFinished = !jsonObject.get("overmax").getAsBoolean();
        return jsonObject.get("result").getAsJsonArray();
    }

    /**
     * Method that resets page to 0 so that 
     * downloads restart from beginning
     */
    public void reset() {
        page = 0;
    }

    /**
     * Method that compares two EventJsonDownloader objects
     * by their startTime
     */
    @Override
    public int compareTo(EventJsonDownloader o) {
        return eventStartTime.compareTo(o.eventStartTime);
    }
}
