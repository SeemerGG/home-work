package com.example.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class UserTest {
    @Test
    @DisplayName("Проверка соответствия логина.")
    void testGetLogin() {
        String login = "userLogin";
        String password = "userPassword";
        User user = new User(login, password);
        assertEquals(login, user.getLogin(), "Геттер getLogin должен возвращать правильный логин.");
    }

    @Test
    @DisplayName("Проверка соответствия пароля.")
    void testGetPassword() {
        String login = "userLogin";
        String password = "userPassword";
        User user = new User(login, password);
        assertEquals(password, user.getPassword(), "Геттер getPassword должен возвращать правильный пароль.");
    }
}
