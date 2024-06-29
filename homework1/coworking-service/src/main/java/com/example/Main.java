package com.example;

import java.sql.Connection;

import com.example.in.controllers.AutentificationController;
import com.example.infrastructure.database.DBSingleton;
import com.example.out.dao.UserDAO;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
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
    public static void main(String[] args) {
        
        Runtime.getRuntime().addShutdownHook(new Thread(() -> { DBSingleton.closeConnection(); }));

        try {
            Connection connection = DBSingleton.getInstance();
            Database database =
                    DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
            Liquibase liquibase =
                    new Liquibase("db/changelog/changelog.xml", new ClassLoaderResourceAccessor(), database);
            liquibase.update();
            System.out.println("Migration is completed successfully");
        } catch (LiquibaseException e) {
            System.out.println("SQL Exception in migration " + e.getMessage());
        }

        AutentificationController controller = new AutentificationController(new UserDAO());
        controller.appRun();
    }
}