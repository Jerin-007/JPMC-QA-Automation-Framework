package utils;

import java.io.FileInputStream;
import java.util.Properties;

public class ConfigReader {
    // Notice: No more static { } block!
    private static Properties properties;

    public static String getProperty(String key) {

        // 1. THE JERRY OPTIMIZATION: Check Cloud Memory FIRST
        String cloudSecret = System.getProperty(key);
        if (cloudSecret != null) {
            return cloudSecret; // We found it in memory! Exit immediately. Zero disk reading required.
        }

        // 2. If Cloud is empty, we must be local. LAZY LOAD the file only when needed.
        if (properties == null) {
            properties = new Properties();
            try {
                FileInputStream input = new FileInputStream("src/test/resources/config.properties");
                properties.load(input);
                input.close();
            } catch (Exception e) {
                // NO MORE BLANK CATCH BLOCKS! We leave a paper trail.
                System.out.println("⚠️ DEBUG: config.properties file not found. System is relying on memory.");
            }
        }

        // 3. Try to grab the key from the file we just loaded
        String localSecret = properties.getProperty(key);
        if (localSecret != null) {
            return localSecret;
        }

        // 4. FAIL FAST: Not in memory, not in the file. Crash the system.
        throw new RuntimeException("CRITICAL ERROR: Could not find '" + key + "' in GitHub Secrets OR config.properties!");
    }
}








/*
package utils;

import java.io.FileInputStream;
import java.util.Properties;

public class ConfigReader {
    private static Properties properties;

    static {
        // 1. Initialize the properties object so it's never null
        properties = new Properties();
        try {
            // We quietly try to load the local file. If it's missing (like in the cloud), that's fine for now.
            FileInputStream input = new FileInputStream("src/test/resources/config.properties");
            properties.load(input);
            input.close();
        } catch (Exception e) {
            // We do NOT throw an error here yet. We wait to see if the Cloud has the key.
        }
    }

    public static String getProperty(String key) {
        // 1. JERRY'S LOGIC: Check Cloud Memory FIRST (Fastest)
        String cloudSecret = System.getProperty(key);
        if (cloudSecret != null) {
            return cloudSecret;
        }

        // 2. Check the Local File SECOND
        String localSecret = properties.getProperty(key);
        if (localSecret != null) {
            return localSecret;
        }

        // 3. JERRY'S LOGIC: Fail Fast!
        // If it's not in the cloud AND not in the file, we violently crash the test immediately.
        throw new RuntimeException("CRITICAL ERROR: Could not find '" + key + "' in GitHub Secrets OR config.properties!");
    }
}
*/








/*

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
*/
