package core;

import org.apache.commons.dbcp2.BasicDataSource;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ahmetkucuk on 28/12/15.
 */
public class DBPrefs {

//    public static final String DB_HOST = "postgres-exposed";
//    public static final String DB_NAME = "isd";
//    public static String DB_USERNAME = "postgres";
//    public static String DB_USER_PASSWORD = "r3mot3p8sswo4d";


    public static final String DB_HOST = "localhost";
    public static final String DB_NAME = "isd";
    public static String DB_USERNAME = "ahmetkucuk";
    public static String DB_USER_PASSWORD = "";

    public static final int DB_PORT = 5432;

    public DBPrefs(){
//    	System.out.println(System.getProperty("os.name"));
//    	System.out.println(System.getProperty("user.name"));
//    	if(System.getProperty("os.name").toLowerCase().equals("linux")){
//    		if(System.getProperty("user.name").equals("berkay")){
//    			DB_USERNAME = "postgres";
//    			DB_USER_PASSWORD = "lakers";
//    		}
//    		if(System.getProperty("user.name").equals("baydin2")){
//    			DB_USERNAME = "berkay";
//    			DB_USER_PASSWORD = "lakers";
//    		}
//
//    	} //else keep it as ahmetkucuk and no password
    }

    public static BasicDataSource getDataSource() {

        Map<String, String> env = System.getenv();
        for (String envName : env.keySet()) {
            System.out.format("%s=%s%n",
                    envName,
                    env.get(envName));
        }

        BasicDataSource dbPoolSourc = new BasicDataSource();
		dbPoolSourc.setSoftMinEvictableIdleTimeMillis(6500);
		dbPoolSourc.setDefaultAutoCommit(true);
		dbPoolSourc.setPoolPreparedStatements(false);
        dbPoolSourc.setDefaultQueryTimeout(60);
        dbPoolSourc.setMinIdle(1);
        dbPoolSourc.setMaxIdle(10);
        dbPoolSourc.setMaxTotal(100);
        dbPoolSourc.setUsername(env.get("POSTGRES_USER"));
        dbPoolSourc.setPassword(env.get("POSTGRES_PASSWORD"));
        dbPoolSourc.setValidationQuery("SELECT 1;");
        dbPoolSourc.setDriverClassName("org.postgresql.Driver");
        dbPoolSourc.setUrl("jdbc:postgresql://" + env.get("POSTGRE_SQL_DB_HOST") + "/" + env.get("POSTGRES_DB"));
        return dbPoolSourc;
    }
    

}

