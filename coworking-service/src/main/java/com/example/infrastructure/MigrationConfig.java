package com.example.infrastructure;

import java.sql.Connection;

import org.springframework.beans.factory.annotation.Autowired;


import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
/**
 * Класс для выполнения миграции.
 */
public class MigrationConfig {

    private static Liquibase liquibase; 

    @Autowired
    private static Properties properties;
    /**
     * Статический метод выполнения миграции на основе значений конфигурационных свойств. 
     */
    @SuppressWarnings("deprecation")
    static public void performingMigration (Connection connection) {

        String liqubaseSchema = properties.getProperty("liquibaseSchema");
        String defaultSchema = properties.getProperty("defaultSchema");
        String changeLogPath = properties.getProperty("changeLogPath");
        try {
            Database database = 
                DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
            database.setDefaultSchemaName(defaultSchema);
            database.setLiquibaseSchemaName(liqubaseSchema);
            liquibase = 
                new Liquibase(changeLogPath, new ClassLoaderResourceAccessor(), database);
            liquibase.update();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Метод закрывающий миграцию.
     */
    static public void closeMigration() {

        try {
            if(liquibase != null) {
                liquibase.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
