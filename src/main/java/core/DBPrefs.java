package core;

import org.apache.commons.dbcp2.BasicDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
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

    public static final int DB_PORT = 5432;


    public static BasicDataSource getDataSource() {

        Map<String, String> env = getTestEnv();

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

    public static Map<String, String> getTestEnv(){
        Map<String, String> env = new HashMap<>();
        env.put("POSTGRES_USER", "postgres");
        env.put("POSTGRES_PASSWORD", "");
        env.put("POSTGRES_DB", "postgres");
        env.put("POSTGRE_SQL_DB_HOST", "localhost");
        return env;
    }


    public static void waitUntilConnected() {

        boolean connected = false;
        Connection ex = null;
        while(!connected) {
            try {
                DataSource dsourc = getDataSource();
                try {
                    ex = dsourc.getConnection();
                    ex.setAutoCommit(true);
                    Statement ex1 = ex.createStatement();
                    connected = ex1.execute("SELECT 1;");
                } catch (SQLException var7) {
                    System.out.println("Failed to Connect. Will retry");
                } finally {
                    if(ex != null) {
                        ex.close();
                    }

                }
            } catch (SQLException var9) {
                var9.printStackTrace();
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Waiting for DB Connection");
        }
        try {
            ex.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    

}

