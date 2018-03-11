package image;

import common.db.DBPrefs;

/**
 * Created by ahmetkucuk on 3/10/18.
 */
public class ImagePullerRunner {

    public static void main(String[] args) {
        System.out.println("Image Puller Runner Started!");
        DBPrefs.waitDefaultDBConnections();
    }
}
