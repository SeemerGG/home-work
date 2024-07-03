package com.example.infrastructure.database;

import java.sql.Connection;
import java.sql.DriverManager;

import com.example.infrastructure.ConfigurationProperties;


public final class DBSingleton {

    private static Connection instance;

    public static Connection getInstance() {
        if(instance == null) {
            try {
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
