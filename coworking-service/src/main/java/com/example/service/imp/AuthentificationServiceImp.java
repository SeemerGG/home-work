package com.example.service.imp;

import org.springframework.stereotype.Service;

import com.example.annotation.Loggable;
import com.example.domain.model.User;
import com.example.out.dao.UserDAO;
import com.example.security.TokenCreator;
import com.example.service.AuthentificationService;

import lombok.RequiredArgsConstructor;

/**
 * Класс реализующий AutentificationService.
 */
@Service
@Loggable
@RequiredArgsConstructor
public class AuthentificationServiceImp implements AuthentificationService {

    private final UserDAO userDAO; 
    private final TokenCreator tokenCreator;

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

