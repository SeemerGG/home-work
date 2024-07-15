package com.example.out.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.example.domain.model.User;


/**
 * Класс для доступа к данным пользователей.
 * Предоставляет методы для работы с пользователями в базе данных.
 */
public final class UserDAO {
    private Connection connection;

    public UserDAO(Connection connection) {
        this.connection = connection;
    }

    /**
     * Получение списка всех пользователей.
     * @return Список пользователей.
     */
    public List<User> getUsers() throws SQLException{
        List<User> users = new ArrayList<>();
        String request = "SELECT * FROM \"user\"";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(request);
        while (resultSet.next()) {
            String login = resultSet.getString("login");
            String password = resultSet.getString("password");
            users.add(new User(login, password));
        }
        return users;
    }

    

    /**
     * Получение пользователя по логину.
     * Использует потоковую обработку списка для поиска пользователя.
     * @param login Логин пользователя для поиска.
     * @return Найденный пользователь или null, если пользователь не найден.
     */
    public User getUser(String login) throws SQLException{
        
        User user = null;
        String request = "SELECT * FROM \"user\" WHERE login=?";
        PreparedStatement statement = connection.prepareStatement(request);
        statement.setString(1, login);
        ResultSet resultSet = statement.executeQuery();
        if(resultSet.next()) {
            user = new User(resultSet.getString("login"), resultSet.getString("password"));
        }
        return user;
    }

    /**
     * Добавление нового пользователя.
     * Создает и добавляет пользователя с указанными логином и хэшированным паролем.
     * @param login Логин нового пользователя.
     * @param hashPassword Хэшированный пароль пользователя.
     */
    public void addUser(String login, String hashPassword) throws SQLException{
        String request = "INSERT INTO \"user\" (login, password) VALUES (?, ?)";
        PreparedStatement statement = connection.prepareStatement(request);
        statement.setString(1, login);
        statement.setString(2, hashPassword);
        statement.executeUpdate();
    }
}
