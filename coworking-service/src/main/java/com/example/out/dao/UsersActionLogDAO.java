package com.example.out.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import javax.sql.DataSource;

import org.springframework.stereotype.Repository;

/**
 * Класс для записи действий пользователей.
 */
@Repository
public class UsersActionLogDAO {

    private Connection connection;

    public UsersActionLogDAO(DataSource dataSource) throws SQLException {

        this.connection = dataSource.getConnection();
    }

    /**
     * Метод добавления в таблицу UsersActionLog.
     */
    public void add(String clientLogin, LocalDateTime requestTime, String url) throws Exception {
        String request = "INSERT INTO \"users_action_log\" (client_login, request_time, url) VALUES (?, ?, ?)";
        PreparedStatement statement = connection.prepareStatement(request);
        statement.setString(1, clientLogin);
        statement.setTimestamp(2, Timestamp.valueOf(requestTime));
        statement.setString(3, url);
        statement.executeUpdate();
    }
}
