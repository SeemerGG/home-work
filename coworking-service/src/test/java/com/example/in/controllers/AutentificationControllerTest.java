package com.example.in.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.security.NoSuchAlgorithmException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;

import com.example.in.security.PasswordHashing;
import com.example.in.views.AutentificationView;
import com.example.model.User;
import com.example.out.dao.UserDAO;

public class AutentificationControllerTest {
    
    // private String login = "user0003";
    // private String password = "user0003";
    // private String hashPassword;
    // @Mock
    // private UserDAO userDAO;
    // @Mock
    // private AutentificationView view;
    // @Mock
    // private MainController mainController;
    // @InjectMocks
    // private AutentificationController autentificationController;

    // @BeforeEach
    // void setUp() throws Exception {
    //     MockitoAnnotations.openMocks(this);
    // }

    // @Test
    // void testAuthorizationSuccess() throws NoSuchAlgorithmException {
    //     String login = "testUser";
    //     String password = "testPass";
    //     User mockUser = new User(login, PasswordHashing.getPasswordHash(password));
    //     when(userDAO.getUser(login)).thenReturn(mockUser);
    //     doNothing().when(mainController).authorized(anyString());
    //     autentificationController.authorization(login, password);
    //     verify(mainController, times(1)).authorized(login);
    // }

    // @Test
    // void testAuthorizationWrongPassword() throws NoSuchAlgorithmException {
    //     String login = "testUser";
    //     String password = "testPass";
    //     String wrongPassword = "wrongPass";
    //     User mockUser = new User(login, PasswordHashing.getPasswordHash(password));
    //     when(userDAO.getUser(login)).thenReturn(mockUser);

    //     verify(view, times(1)).sayError("Неверный пароль!");

    // }

    // @Test
    // void testAuthorizationUserNotFound() {
    //     String login = "nonExistentUser";
    //     when(userDAO.getUser(login)).thenReturn(null);

    //     verify(view, times(1)).sayError("Пользователя с таким логином не существует!");
    // }

    // @Test
    // void testRegistrationSuccess() throws NoSuchAlgorithmException {
    //     String login = "newUser";
    //     String password = "newPass";
    //     String hashedPassword = PasswordHashing.getPasswordHash(password);
    //     doNothing().when(userDAO).addUser(anyString(), anyString());
    //     when(PasswordHashing.getPasswordHash(password)).thenReturn(hashedPassword);

    //     autentificationController.registration(login, password);

    //     verify(mainController, times(1)).authorized(login);
    // }
}
