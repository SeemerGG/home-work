package com.example.infrastructure.database;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;

import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.Configuration;

public final class DBSingleton {

    private static Connection instance;

    public static Connection getInstance() {
        if(instance == null) {
            Configurations configs = new Configurations();
            try {
                Configuration config = configs.properties(new File("application.properties"));
                String dbHost = config.getString("db.host");
                String dbUser = config.getString("db.user");
                String dbPassword = config.getString("db.password");
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
