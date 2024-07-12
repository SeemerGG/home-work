package com.example.infrastructure.database;

import java.sql.Connection;
import java.sql.DriverManager;

import com.example.infrastructure.ConfigurationProperties;

/**
 * Класс предоставляющий подключение к базе данных.
 */
public final class DBSingleton {

    private static Connection instance;

    /**
     * Метод предоставляющий подключение к базе данных.
     * @return
     */
    public static Connection getInstance() {

        if(instance == null) {
            try {
                Class.forName("org.postgresql.Driver");
                String dbHost = ConfigurationProperties.properties.getProperty("db.host");
                String dbUser = ConfigurationProperties.properties.getProperty("db.user");
                String dbPassword = ConfigurationProperties.properties.getProperty("db.password");
                instance = DriverManager.getConnection(dbHost, dbUser, dbPassword);
                instance.setAutoCommit(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return instance;
    }

    /**
     * Метод закрывает подключение.
     */
    public static void closeConnection() {

        if (instance != null) {
            try {
                instance.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
