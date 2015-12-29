package core;

/**
 * Created by ahmetkucuk on 28/12/15.
 */
public class DBPrefs {

    public static final String DB_HOST = "localhost";
    public static final String DB_NAME = "dbproject";
    public static String DB_USERNAME = "ahmetkucuk";
    public static String DB_USER_PASSWORD = "";
    public static final int DB_PORT = 5432;

    public DBPrefs(){
    	if(System.getProperty("os.name").toLowerCase().equals("linux")){
    		if(System.getProperty("user.name").equals("berkay")){
    			DB_USERNAME = "postgres";
    			DB_USER_PASSWORD = "lakers";
    		}
    	} //else keep it as ahmetkucuk and no password
    }
    

}

