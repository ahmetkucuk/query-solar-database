package utils;

import java.io.*;

/**
 * Created by ahmetkucuk on 23/11/15.
 */
public class FileManager {

    private static final FileManager instance = new FileManager();
    private ClassLoader classLoader = null;


    private FileManager() {
        classLoader = this.getClass().getClassLoader();
    }

    public static FileManager getInstance() {return instance;}

    public String getResourcePath(String fileName) {
        return classLoader.getResource(fileName) == null ? null : classLoader.getResource(fileName).getPath();
    }

    public InputStream getInputStream(String fileName) {
        InputStream inputStream = instance.getClass().getResourceAsStream(fileName);
        if(inputStream != null) {
            return inputStream;
        }
        try {
            inputStream = new FileInputStream(new File(getResourcePath(fileName)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return inputStream;
    }

}
