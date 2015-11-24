package utils;

import java.io.File;

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

    public String getPath(String fileName) {
        return classLoader.getResource(fileName) == null ? null : classLoader.getResource(fileName).getPath();
    }

}
