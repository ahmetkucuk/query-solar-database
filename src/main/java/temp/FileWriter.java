package temp;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;

/**
 * Created by ahmetkucuk on 25/10/15.
 */
public class FileWriter {
    protected BufferedWriter out;
    protected String outputFile;

    protected static final String SEPARATOR = "\t";

    public FileWriter(String outputFile) {
        this.outputFile = outputFile;

    }

    public void start() {
        openWriteFile();
    }

    public void finish() {
        closeFile();
    }

    public synchronized void flush() {
        if(out != null) {
            try {
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    protected void openWriteFile() {

        java.io.FileWriter fStream = null;
        try {
            createFileDirectoryIfNotExists();
            fStream = new java.io.FileWriter(outputFile, true);
            out = new BufferedWriter(fStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createFileDirectoryIfNotExists() {

        File f = new File(outputFile);
        if(!f.exists()) f.getParentFile().mkdirs();
    }

    protected void closeFile() {
        if(out != null) {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Write the parsed input file
     *
     * @param fn
     */
    public synchronized void writeToFile(String fn) {
        try {
            out.write(fn);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
