package com.example.out.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.File;
import java.sql.Connection;
import java.util.stream.Stream;

import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.lifecycle.Startables;

import com.example.in.security.PasswordHashing;
import com.example.model.User;

import liquibase.Contexts;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;

import org.apache.commons.configuration2.Configuration;

public class UserDAOTest {

    private static UserDAO userDAO;

    private static Connection connection;

    @Container
    private static PostgreSQLContainer<?> container;


    static {
        Configurations configs = new Configurations();
        try {
            Configuration config = configs.properties(new File("application.properties"));
            String containerVer = config.getString("container.version");
            container = new PostgreSQLContainer<>(containerVer);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @SuppressWarnings("deprecation")
    @BeforeAll 
    static void runConfiguration() {
        try {
            Startables.deepStart(Stream.of(container)).join();
            connection = container.createConnection("");
            Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
            Liquibase liquibase = new Liquibase("db/changelog/changelog.xml", new ClassLoaderResourceAccessor(), database);
            liquibase.update(new Contexts());
            liquibase.close();
            userDAO = new UserDAO(connection);
        } catch (Exception e) {
            System.out.print(e.getMessage());
        }
    } 

    @Test
    @DisplayName("Проверка добавления пользователя.")
    void testAddUser() {
        try {
            userDAO.addUser("user0003", PasswordHashing.getPasswordHash("user0003"));
            assertNotNull(userDAO.getUser("user0003"));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    @DisplayName("Проверка метода получения пользователя по его логину.")
    void testGetUser() {
        try {
            User user = userDAO.getUser("user0003");
            assertNotNull(user, "Пользователь с логином user0003 должен существовать");
            assertEquals("user0003", user.getLogin(), "Логин пользователя должен соответствовать запрошенному");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
