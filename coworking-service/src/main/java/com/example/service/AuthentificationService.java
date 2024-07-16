package com.example.service;

import com.example.domain.model.User;

/**
 * Интерфейс задающий логику аутентификации пользователя.
 */
public interface AuthentificationService {

    /**
     * Метод для авторизации пользователя.
     * @param user Объект User.
     */
    public String authorization(User user) throws Exception;

    /**
     * Метод для авторизации пользователя.
     * @param user Объект User.
     */
    public String registration(User user) throws Exception;
}
