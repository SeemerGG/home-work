package com.example.infrastructure;

import java.sql.Connection;

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
    /**
     * Статический метод выполнения миграции на основе значений конфигурационных свойств. 
     */
    @SuppressWarnings("deprecation")
    static public void performingMigration (Connection connection) {

        String liqubaseSchema = ConfigurationProperties.properties.getProperty("liquibase.currentSchema");
        String defaultSchema = ConfigurationProperties.properties.getProperty("liquibase.defaultSchema");
        String changeLogPath = ConfigurationProperties.properties.getProperty("liquibase.changeLogPath");
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
