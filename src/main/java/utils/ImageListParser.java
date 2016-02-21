package utils;

import models.ImageAttributes;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by ahmetkucuk on 21/02/16.
 */
public class ImageListParser {

    private BufferedReader reader;

    public ImageListParser(String inputFileName) {
        init(inputFileName);
    }

    private void init(String fileName) {
        FileInputStream fStream1;
        try {
            fStream1 = new FileInputStream(fileName);
            DataInputStream in = new DataInputStream(fStream1);
            reader = new BufferedReader(new InputStreamReader(in));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    public ImageAttributes next() {

        String line;
        try {
            line = reader.readLine();
            if(line == null) return null;

            line = line.substring(line.lastIndexOf("/")+1, line.length() - 4);

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy_M_dd'__'HH_mm_ss_SSS");
            Date date = formatter.parse(line.substring(0, 23));
            String [] tokens = line.split("_");

            return  new ImageAttributes(date, tokens[tokens.length-1], tokens[tokens.length-4], tokens[tokens.length-2], line);



        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;

    }

    public void finish() {
        onDestroy();
    }

    private void onDestroy() {
        try {
            if(reader != null)
                reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
