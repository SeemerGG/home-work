package com.example.service.imp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.annotation.Loggable;
import com.example.domain.model.User;
import com.example.out.dao.UserDAO;
import com.example.security.TokenCreator;
import com.example.service.AuthentificationService;

/**
 * Класс реализующий AutentificationService.
 */
@Service
@Loggable
public class AuthentificationServiceImp implements AuthentificationService {

    private final UserDAO userDAO; 
    private final TokenCreator tokenCreator;

    /**
     * Конструктор класса.
     */
    @Autowired
    public AuthentificationServiceImp(UserDAO userDAO, TokenCreator tokenCreator) {

        this.userDAO = userDAO;
        this.tokenCreator = tokenCreator;
    }

    @Override
    public String authorization(User user) throws Exception {

        User realUser = userDAO.getUser(user.getLogin());
        if(realUser != null) {
            if(realUser.getPassword().equals(user.getPassword())) {
                return tokenCreator.generateToken(user.getLogin());
            }
            else {
                throw new Exception("Неверный пароль.");
            }
        }
        else {
            throw new Exception("Пользователя с таким логином не существует!");
        }
    }

    @Override
    public String registration(User user) throws Exception {
        
        if(userDAO.getUser(user.getLogin()) != null) {
            throw new Exception("Пользователь с указанным логином уже существует.");
        }

        userDAO.addUser(user.getLogin(), user.getPassword());
        return tokenCreator.generateToken(user.getLogin());
    }
}

