package com.example.out.dao;

import java.util.ArrayList;
import java.util.List;

import com.example.model.User;

/**
 * Класс для доступа к данным пользователей.
 * Предоставляет методы для работы с пользователями в базе данных.
 */
public class UserDAO {
    private List<User> users;

    {
        users = new ArrayList<>();
        
        users.add(new User("user0001", "36e5eebcf7aee8f72bc168e0f8ae6eed00d60446e9f7c03989ddea84ac7c8711"));
        users.add(new User("user0002", "ae9a6aa038a92a456dc27a69746da4ec4f4fc490d411711c0f6c5ec01ba21656"));
    }

    /**
     * Получение списка всех пользователей.
     * @return Список пользователей.
     */
    public List<User> getUsers() {
        return users;
    }

    /**
     * Получение пользователя по логину.
     * Использует потоковую обработку списка для поиска пользователя.
     * @param login Логин пользователя для поиска.
     * @return Найденный пользователь или null, если пользователь не найден.
     */
    public User getUser(String login) {
        return users.stream().filter(user -> user.getLogin().equals(login)).findAny().orElse(null);
    }

    /**
     * Добавление нового пользователя.
     * Создает и добавляет пользователя с указанными логином и хэшированным паролем.
     * @param login Логин нового пользователя.
     * @param hashPassword Хэшированный пароль пользователя.
     */
    public void addUser(String login, String hashPassword) {
        users.add(new User(login, hashPassword));
    }
}
