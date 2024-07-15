package com.example.service.imp;

import com.example.annotation.Loggable;
import com.example.domain.model.User;
import com.example.out.dao.UserDAO;
import com.example.security.TokenCreator;

/**
 * Класс реализующий AutentificationService.
 */
@Loggable
public class AuthentificationServiceImp {

    private final UserDAO userDAO; 

    /**
     * Конструктор класса.
     */
    public AuthentificationServiceImp(UserDAO userDAO) {

        this.userDAO = userDAO;
    }

    public String authorization(User user) throws Exception {

        User realUser = userDAO.getUser(user.getLogin());
        if(realUser != null) {
            if(realUser.getPassword().equals(user.getPassword())) {
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

    public String registration(User user) throws Exception {
        
        if(userDAO.getUser(user.getLogin()) != null) {
            throw new Exception("Пользователь с указанным логином уже существует.");
        }

        userDAO.addUser(user.getLogin(), user.getPassword());
        return TokenCreator.generateToken(user.getLogin());
    }
}

