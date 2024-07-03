package com.example.infrastructure;

import java.io.InputStream;
import java.util.Properties;

/**
 * Класс хранящий объект Properties для удобного обращения к свойствам.
 */
public class ConfigurationProperties {

    public static Properties properties;

    static {
        properties = new Properties();
        try (InputStream input = ConfigurationProperties.class.getClassLoader().getResourceAsStream("application.properties")){
            properties.load(input);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
