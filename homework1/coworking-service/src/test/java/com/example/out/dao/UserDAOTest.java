package com.example.out.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.sql.Connection;
import java.util.stream.Stream;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.lifecycle.Startables;

import com.example.in.security.PasswordHashing;
import com.example.infrastructure.ConfigurationProperties;
import com.example.infrastructure.MigrationConfig;
import com.example.model.User;

public class UserDAOTest {

    private static UserDAO userDAO;

    private static Connection connection;

    @Container
    private static PostgreSQLContainer<?> container;


    static {
        try {
            String containerVer = ConfigurationProperties.properties.getProperty("container.version");
            container = new PostgreSQLContainer<>(containerVer);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @BeforeAll 
    static void runConfiguration() throws Exception {

        Startables.deepStart(Stream.of(container)).join();
        connection = container.createConnection("");
        MigrationConfig.performingMigration(connection);
        userDAO = new UserDAO(connection);
    } 

    @Test
    @DisplayName("Проверка добавления пользователя.")
    void testAddUser() throws Exception {

        userDAO.addUser("user0003", PasswordHashing.getPasswordHash("user0003"));
        assertNotNull(userDAO.getUser("user0003"));
    }

    @Test
    @DisplayName("Проверка метода получения пользователя по его логину.")
    void testGetUser() throws Exception {

        User user = userDAO.getUser("user0003");
        assertNotNull(user, "Пользователь с логином user0003 должен существовать");
        assertEquals("user0003", user.getLogin(), "Логин пользователя должен соответствовать запрошенному");
    }

    @AfterAll
    static void down() {
        
        MigrationConfig.closeMigration();
        container.stop();
    } 
}
