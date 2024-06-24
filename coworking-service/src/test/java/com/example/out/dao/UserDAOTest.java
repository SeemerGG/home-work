package com.example.out.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.example.model.User;

public class UserDAOTest {
    private UserDAO userDAO;

    @BeforeEach
    void setUp() {
        userDAO = new UserDAO();
        userDAO.addUser("user0003", "user0003");
    }

    @Test
    void testGetUsers() {
        assertFalse(userDAO.getUsers().isEmpty(), "Список пользователей не должен быть пустым");
    }

    @Test
    void testGetUser() {
        User user = userDAO.getUser("user0003");
        assertNotNull(user, "Пользователь с логином user0003 должен существовать");
        assertEquals("user0003", user.getLogin(), "Логин пользователя должен соответствовать запрошенному");
    }
    
}
