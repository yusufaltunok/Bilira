package bilira.utilities;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConfigReader {
static Connection connection;


    public static String getProperty(String key){

        Properties properties = new Properties();

        try {
            FileInputStream fis = new FileInputStream("configuration.properties");
            properties.load(fis);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return properties.getProperty(key);
    }

    public static Connection getDbProperty(String dbUrl, String dbUsername, String dbPassword){
        Properties properties = new Properties();
        try {
            FileInputStream fis = new FileInputStream("configuration.properties");
            properties.load(fis);
          connection =  DriverManager.getConnection(properties.getProperty(dbUrl), properties.getProperty(dbUsername), properties.getProperty(dbPassword));
        } catch (IOException | SQLException e) {
            throw new RuntimeException(e);
        }
        return connection;
    }

    private static Properties properties = new Properties();
    public static boolean useRemoteDebugging() {
        return Boolean.parseBoolean(properties.getProperty("remoteDebug", "true"));  // false varsayılan
    }

}
