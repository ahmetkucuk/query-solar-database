package core;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by ahmetkucuk on 20/12/15.
 */
public class EventJsonDownloader {

    private String eventType;
    private String eventStartTime;
    private String eventEndTime;
    private int resultLimit;
    private int page = 1;
    private boolean isFinished = false;

    //Event Type, page, event start time, event end time
    private static final String URL = "https://www.lmsal.com/hek/her?cosec=2&cmd=search&type=column&event_type=%s&event_region=all&event_coordsys=helioprojective&x1=-5000&x2=5000&y1=-5000&y2=5000&result_limit=%d&page=%d&event_starttime=%s&event_endtime=%s";

    public EventJsonDownloader(String eventType, String eventStartTime, String eventEndTime, int resultLimit) {
        this.eventType = eventType;
        this.eventStartTime = eventStartTime;
        this.eventEndTime = eventEndTime;
        this.resultLimit = resultLimit;
    }

    public JsonArray next() {
        if(isFinished) return null;

        String url = getNextUrl();
        System.out.println(url);
        String downloaded = downloadByUrl(url);
        if(downloaded == null) return new JsonArray();

        return getResultArray(downloaded);

    }


    private String getNextUrl() {
        String result = String.format(URL, eventType, resultLimit, page, eventStartTime, eventEndTime);
        page++;
        return result;
    }

    private String downloadByUrl(String url) {

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new URL(url).openStream()))) {
            StringBuilder sb = new StringBuilder();

            String inputLine;

            //Build a string from streamed data
            while ((inputLine = reader.readLine()) != null) {
                sb.append(inputLine + "\n");
            }
            return sb.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private JsonArray getResultArray(String result) {
        JsonParser parser = new JsonParser();
        JsonElement jsonElement = parser.parse(result);
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        isFinished = !jsonObject.get("overmax").getAsBoolean();
        return jsonObject.get("result").getAsJsonArray();
    }
}
