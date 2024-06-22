package com.example.in.controllers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.security.NoSuchAlgorithmException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.in.security.PasswordHashing;
import com.example.in.views.AutentificationView;
import com.example.model.User;
import com.example.out.dao.UserDAO;

public class AutenrificationControllerTest {
    
    @Mock
    private UserDAO userDAO;
    @Mock
    private AutentificationView view;
    @Mock
    private MainController mainController;
    @InjectMocks
    private AutentificationController autentificationController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        autentificationController = new AutentificationController(userDAO);
    }

    @Test
    void testAuthorizationSuccess() throws NoSuchAlgorithmException {
        // Подготовка
        String login = "testUser";
        String password = "testPass";
        User mockUser = new User(login, PasswordHashing.getPasswordHash(password));
        when(userDAO.getUser(login)).thenReturn(mockUser);
        when(PasswordHashing.compareHashAndString(mockUser.getPassword(), password)).thenReturn(true);

        // Выполнение
        autentificationController.authorization(login, password);

        // Проверка
        verify(mainController, times(1)).authorized(login);
    }

    @Test
    void testAuthorizationWrongPassword() throws NoSuchAlgorithmException {
        // Подготовка
        String login = "testUser";
        String password = "testPass";
        String wrongPassword = "wrongPass";
        User mockUser = new User(login, PasswordHashing.getPasswordHash(password));
        when(userDAO.getUser(login)).thenReturn(mockUser);
        when(PasswordHashing.compareHashAndString(mockUser.getPassword(), wrongPassword)).thenReturn(false);

        // Выполнение
        autentificationController.authorization(login, wrongPassword);

        // Проверка
        verify(view, times(1)).sayError("Неверный пароль!");
    }

    @Test
    void testAuthorizationUserNotFound() throws NoSuchAlgorithmException {
        // Подготовка
        String login = "nonExistentUser";
        when(userDAO.getUser(login)).thenReturn(null);

        // Выполнение
        autentificationController.authorization(login, "anyPassword");

        // Проверка
        verify(view, times(1)).sayError("Пользователя с таким логином не существует!");
    }

    // Дополнительные тесты для метода registration...
}
