package utils;

import java.io.FileInputStream;
import java.util.Properties;

public class ConfigReader {
    private static Properties properties;

    static {
        try{
            // Point Java to the secret file
            String path = "src/test/resources/config.properties";
            FileInputStream input = new FileInputStream(path);
            properties = new Properties();
            properties.load(input);
            input.close();
        }catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load config.properties file!");
        }
    }

    // The method our tests will use to ask for the secret
    public static String getProperty(String key) {
        // 1. Check if the Cloud Pipeline passed the wecret directly to Java
        String cloudSecret = System.getProperty(key);
        if (cloudSecret != null) {
            return cloudSecret; // We are in the cloud! Use this!
        }

        // 2. If cloudSecret is null, we must be on Jerry's local laptop. Open the file.
        return properties.getProperty(key);
    }
}
