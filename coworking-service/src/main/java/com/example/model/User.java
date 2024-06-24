package com.example.model;

/**
 * Класс User представляет собой модель пользователя для системы.
 * Он содержит основные учетные данные пользователя, такие как логин и пароль.
 */
public class User {
    private String login;
    private String password;

    /**
     * Конструктор для создания нового пользователя.
     * @param login Логин пользователя.
     * @param password Пароль пользователя.
     */
    public User(String login, String password) {
        this.login = login;
        this.password = password;
    }

    /**
     * Получить логин пользователя.
     * @return Возвращает логин пользователя.
     */
    public String getLogin() {
        return login;
    }

    /**
     * Получить пароль пользователя.
     * @return Возвращает пароль пользователя.
     */
    public String getPassword() {
        return password;
    }

}