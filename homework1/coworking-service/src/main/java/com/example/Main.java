package com.example;

import java.io.File;
import java.sql.Connection;


import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.builder.fluent.Configurations;

import com.example.in.controllers.AutentificationController;
import com.example.infrastructure.database.DBSingleton;
import com.example.out.dao.UserDAO;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;

/**
 * Главный класс приложения, отвечающий за запуск программы.
 */
public class Main {

    /**
     * Основной метод приложения.
     * Создает экземпляр AutentificationController с новым UserDAO, запускает основной цикл приложения.
     * @param args аргументы командной строки (не используются)
     */
    @SuppressWarnings("deprecation")
    public static void main(String[] args) {
        Configurations configs = new Configurations();
        try {
            Configuration config = configs.properties(new File("application.properties"));
            String liquibaseSchema = config.getString("liquibase.currentSchema");
            String defaultSchema = config.getString("liquibase.defaultSchema");
            Connection connection = DBSingleton.getInstance();
            Database database =
                    DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
            database.setDefaultSchemaName(defaultSchema);
            database.setLiquibaseSchemaName(liquibaseSchema);
            Liquibase liquibase =
                    new Liquibase("db/changelog/changelog.xml", new ClassLoaderResourceAccessor(), database);
            liquibase.update();
            System.out.println("Migration is completed successfully");

            AutentificationController controller = new AutentificationController(new UserDAO(DBSingleton.getInstance()));
            controller.appRun();
            liquibase.close();
        } catch (Exception e) {
            System.out.println("SQL Exception in migration " + e.getMessage());
        }
        finally {
            DBSingleton.closeConnection();
        }
        
    }
}