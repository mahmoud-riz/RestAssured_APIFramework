package com.automation.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Configuration manager to handle loading and accessing configuration properties
 */
@Slf4j
public class ConfigManager {
    private static final String CONFIG_FILE = "config.properties";
    private static Properties properties;
    private static ConfigManager instance;

    private ConfigManager() {
        loadProperties();
    }

    /**
     * Get singleton instance of ConfigManager
     */
    public static synchronized ConfigManager getInstance() {
        if (instance == null) {
            instance = new ConfigManager();
        }
        return instance;
    }

    /**
     * Load properties from configuration file
     */
    private void loadProperties() {
        properties = new Properties();
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (inputStream == null) {
                throw new RuntimeException("Configuration file '" + CONFIG_FILE + "' not found in classpath");
            }
            properties.load(inputStream);
            log.info("Configuration properties loaded successfully from {}", CONFIG_FILE);
        } catch (IOException e) {
            log.error("Failed to load configuration properties from {}", CONFIG_FILE, e);
            throw new RuntimeException("Failed to load configuration properties", e);
        }
    }

    /**
     * Get property value by key
     */
    public String getProperty(String key) {
        String value = System.getProperty(key);
        if (value == null) {
            value = properties.getProperty(key);
        }
        if (value == null) {
            log.warn("Property '{}' not found in configuration", key);
        }
        return value;
    }

    /**
     * Get property value with default fallback
     */
    public String getProperty(String key, String defaultValue) {
        String value = getProperty(key);
        return value != null ? value : defaultValue;
    }

    /**
     * Get integer property value
     */
    public int getIntProperty(String key) {
        String value = getProperty(key);
        if (value == null) {
            throw new RuntimeException("Property '" + key + "' not found");
        }
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new RuntimeException("Property '" + key + "' is not a valid integer: " + value, e);
        }
    }

    /**
     * Get integer property value with default fallback
     */
    public int getIntProperty(String key, int defaultValue) {
        try {
            return getIntProperty(key);
        } catch (RuntimeException e) {
            log.warn("Using default value {} for property {}", defaultValue, key);
            return defaultValue;
        }
    }

    /**
     * Get boolean property value
     */
    public boolean getBooleanProperty(String key) {
        String value = getProperty(key);
        if (value == null) {
            throw new RuntimeException("Property '" + key + "' not found");
        }
        return Boolean.parseBoolean(value);
    }

    /**
     * Get boolean property value with default fallback
     */
    public boolean getBooleanProperty(String key, boolean defaultValue) {
        try {
            return getBooleanProperty(key);
        } catch (RuntimeException e) {
            log.warn("Using default value {} for property {}", defaultValue, key);
            return defaultValue;
        }
    }

    // Convenience methods for commonly used properties
    public String getBaseUrl() {
        return getProperty("api.base.url");
    }

    public String getUsersEndpoint() {
        return getProperty("api.users.endpoint");
    }

    public String getApiToken() {
        return getProperty("api.token", ""); // Default to empty string if not configured
    }

    public int getRequestTimeout() {
        return getIntProperty("request.timeout", 30000);
    }

    public int getConnectionTimeout() {
        return getIntProperty("connection.timeout", 10000);
    }

    public String getEnvironment() {
        return getProperty("environment", "qa");
    }

    public int getRetryCount() {
        return getIntProperty("retry.count", 2);
    }

    public int getRetryInterval() {
        return getIntProperty("retry.interval", 1000);
    }
} 