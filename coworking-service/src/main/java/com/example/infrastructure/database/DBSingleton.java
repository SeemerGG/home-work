package com.example.infrastructure.database;

import java.sql.Connection;
import java.sql.DriverManager;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.infrastructure.Properties;

/**
 * Класс предоставляющий подключение к базе данных.
 */
public final class DBSingleton {

    private static Connection instance;

    @Autowired
    private static Properties properties;

    /**
     * Метод предоставляющий подключение к базе данных.
     * @return
     */
    public static Connection getInstance() {

        
        if(instance == null) {
            try {
                Class.forName("org.postgresql.Driver");
                String dbHost = properties.getProperty("dbHost");
                String dbUser = properties.getProperty("dbUser");
                String dbPassword = properties.getProperty("dbPassword");
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
