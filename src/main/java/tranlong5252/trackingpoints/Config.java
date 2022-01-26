package tranlong5252.trackingpoints;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class Config {

    static final String fileName = "config.yml";
    static TrackingPoints main = TrackingPoints.getMain();
    public static File file;
    public static YamlConfiguration config;
    public static String host;
    public static String db;
    public static String user;
    public static String pwd;
    public static int port;


    public static void init() {
        main.getDataFolder().mkdirs();
        file = getConfigFile();
        if(!file.exists()) {
            main.saveDefaultConfig();
        }
        loadConfig();
    }
    
    public static File getConfigFile() {
    	return new File(main.getDataFolder() + "/" + fileName);
    }

    public static void loadConfig() {
        config = YamlConfiguration.loadConfiguration(file);
        loadMySQLHost();
        loadMySQLDB();
        loadMySQLUser();
        loadMySQLPwd();
        loadMySQLPort();
    }

    public static void saveFileF() {
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadMySQLHost() {
        host = config.getString("mysql.host");
    }

    public static void loadMySQLUser() {
        user = config.getString("mysql.user");
    }

    public static void loadMySQLPwd() {
        pwd = config.getString("mysql.password");
    }

    public static void loadMySQLDB() {
        db = config.getString("mysql.dbname");
    }

    public static void loadMySQLPort() {
        port = config.getInt("mysql.port", 3306);
    }
}
