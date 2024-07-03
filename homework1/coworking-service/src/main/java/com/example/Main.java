package com.example;

import com.example.in.controllers.AutentificationController;
import com.example.infrastructure.MigrationConfig;
import com.example.infrastructure.database.DBSingleton;
import com.example.out.dao.UserDAO;
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
        try {
            MigrationConfig.performingMigration(DBSingleton.getInstance());
            AutentificationController controller = new AutentificationController(new UserDAO(DBSingleton.getInstance()));
            controller.appRun();
            MigrationConfig.closeMigration();
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            DBSingleton.closeConnection();
        }
    }
}