package com.example;


import com.example.infrastructure.MigrationConfig;
import com.example.infrastructure.database.DBSingleton;
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
            MigrationConfig.closeMigration();
            DBSingleton.getInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            DBSingleton.closeConnection();
        }
    }
}