package com.productivitytracker.config;

import com.productivitytracker.exception.ConfigurationException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Central configuration loader for classpath properties and environment overrides.
 */
public final class AppConfig {

    private static final Properties PROPERTIES = loadProperties();

    private AppConfig() {
        throw new AssertionError("AppConfig should not be instantiated");
    }

    /**
     * Reads a property by key. Environment variables override property files ONLY for non-critical settings.
     * Database credentials are loaded from property files only to prevent accidental misconfigurations.
     * Example: db.url can be overridden by DB_URL ONLY if it's explicitly set and valid.
     */
    public static String get(String key, String defaultValue) {
        String environmentKey = key.toUpperCase().replace('.', '_').replace('-', '_');
        String environmentValue = System.getenv(environmentKey);
        
        // Skip environment override for critical database properties if they're not valid
        if (environmentValue != null && !environmentValue.isBlank()) {
            // Allow environment override for db.url only if it's a valid JDBC URL
            if ("DB_URL".equals(environmentKey)) {
                if (environmentValue.startsWith("jdbc:") && 
                    (environmentValue.contains("jdbc:mysql:") || environmentValue.contains("jdbc:sqlite:") || 
                     environmentValue.contains("jdbc:postgresql:") || environmentValue.contains("jdbc:h2:"))) {
                    // Valid JDBC URL found in environment
                    System.out.println("Using database URL from environment: " + environmentValue);
                    return environmentValue;
                } else {
                    // Invalid JDBC URL in environment, ignore it
                    System.out.println("WARNING: Invalid JDBC URL in DB_URL environment variable. Using default: " + (PROPERTIES.getProperty(key, defaultValue)));
                    return PROPERTIES.getProperty(key, defaultValue);
                }
            }
            // For other properties, allow environment override
            return environmentValue;
        }

        return PROPERTIES.getProperty(key, defaultValue);
    }

    public static String getRequired(String key) {
        String value = get(key, null);
        if (value == null || value.isBlank()) {
            throw new ConfigurationException("Missing required configuration: " + key);
        }
        return value;
    }

    public static int getInt(String key, int defaultValue) {
        String value = get(key, String.valueOf(defaultValue));
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException ex) {
            throw new ConfigurationException("Invalid integer configuration: " + key, ex);
        }
    }

    public static boolean getBoolean(String key, boolean defaultValue) {
        return Boolean.parseBoolean(get(key, String.valueOf(defaultValue)));
    }

    private static Properties loadProperties() {
        Properties properties = new Properties();
        load(properties, "app.properties");
        load(properties, "database.properties");
        return properties;
    }

    private static void load(Properties properties, String resourceName) {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        try (InputStream inputStream = loader.getResourceAsStream(resourceName)) {
            if (inputStream != null) {
                properties.load(inputStream);
            }
        } catch (IOException ex) {
            throw new ConfigurationException("Failed to load " + resourceName, ex);
        }
    }
}
