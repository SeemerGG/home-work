package com.example.in.service;

import java.security.NoSuchAlgorithmException;

import com.example.in.security.PasswordHashing;
import com.example.in.security.TokenCreator;
import com.example.infrastructure.database.DBSingleton;
import com.example.model.User;
import com.example.out.dao.UserDAO;

/**
 * Класс AutentificationService содержит логику аутентификации пользователя.
 */
public class AutentificationService {

    private final UserDAO userDAO; 

    /**
     * Конструктор класса.
     */
    public AutentificationService() {
        this.userDAO = new UserDAO(DBSingleton.getInstance());
    }

    /**
     * Метод для авторизации пользователя.
     * @param user Объект User.
     * @throws Exception 
     */
    public String authorization(User user) throws Exception {
        User realUser = userDAO.getUser(user.getLogin());
        if(realUser != null) {
            if(PasswordHashing.compareHashAndString(realUser.getPassword(), user.getPassword())) {
                return TokenCreator.generateToken(user.getLogin());
            }
            else {
                throw new Exception("Неверный пароль.");
            }
        }
        else {
            throw new Exception("Пользователя с таким логином не существует!");
        }
    }

    /**
     * Метод для регистрации нового пользователя.
     * @param user Объект класса User.
     * @throws NoSuchAlgorithmException Если алгоритм хеширования не поддерживается.
     */
    public String registration(User user) throws Exception {
        String hashPassword = PasswordHashing.getPasswordHash(user.getPassword());
        if(userDAO.getUser(user.getLogin()) != null) {
            throw new Exception("Пользователь с указанным логином уже существует.");
        }

        userDAO.addUser(user.getLogin(), hashPassword);
        return TokenCreator.generateToken(user.getLogin());
    }
}

