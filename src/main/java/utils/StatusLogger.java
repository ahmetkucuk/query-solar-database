package utils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ahmetkucuk on 23/06/16.
 */
public class StatusLogger {

    private List<String> listOfDownloadedUrls = new ArrayList<>();

    private static final String LATEST_DOWNLOADED = "latest_downloaded.txt";
    private static final String DOWNLOADED_URLS = "downloaded_urls.txt";


    private static StatusLogger statusLogger;
    private String location;

    private StatusLogger(String location) {
        this.location = location;
    }

    public static void init(String location) {
        statusLogger = new StatusLogger(location);
    }

    public static StatusLogger getInstance() {
        if(statusLogger == null) {
            System.err.println("You need to init status logger!");
        }
        return statusLogger;
    }

    public void writeDataFile(String fileName, String content) {
        try(FileWriter writer = new FileWriter(new File(location, fileName))) {
            writer.write(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeUrls(String url) {

        try(FileWriter writer = new FileWriter(new File(location, DOWNLOADED_URLS))) {
            writer.write(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeLatestDownloaded(String date) {

        try(FileWriter writer = new FileWriter(new File(location, LATEST_DOWNLOADED))) {
            writer.write(date);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String readLatestFile(int index) {
        try (BufferedReader reader = new BufferedReader(new FileReader(new File(location, LATEST_DOWNLOADED)))) {
            String line = reader.readLine();
            if(line == null) {
                throw new Exception("Latest Downloaded file cannot be found at " + location + LATEST_DOWNLOADED);
            }
            return line.split("SEP")[index];
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return "-1000";
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public int getLatestDownloadedPage() {
        return Integer.parseInt(readLatestFile(2));
    }
    public String getLatestDownloadedStartTime() {
        return readLatestFile(0);
    }
    public String getLatestDownloadedEndTime() {
        return readLatestFile(1);
    }

    private void readUrls(String location) {
        try (BufferedReader reader = new BufferedReader(new FileReader(new File(location, DOWNLOADED_URLS)))) {
            String line;
            while((line = reader.readLine()) != null) {
                listOfDownloadedUrls.add(line);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
