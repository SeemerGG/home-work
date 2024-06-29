package com.example.out.dao;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


import java.sql.Date;
import java.sql.Time;

import com.example.infrastructure.database.DBSingleton;
import com.example.model.Reservation;

/**
 * Класс для доступа к данным о бронированиях.
 * Позволяет управлять бронированиями мест в системе.
 */
public final class ReservationDAO {

    private Connection connection;
    
    /**
     * Конструктор класса. 
     */
    public ReservationDAO() {
        connection = DBSingleton.getInstance();
    }

    /**
     * Получение списка всех бронирований.
     * @return Список бронирований.
     */
    public Map<Integer, Reservation> getReservations() throws SQLException{
        String request = "SELECT * FROM Reservation";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(request);
        return resultForMap(resultSet);
    }

    private Map<Integer, Reservation> resultForMap(ResultSet resultSet) throws SQLException {
        Map<Integer, Reservation> reservations = new HashMap<>();
        while(resultSet.next()) {
            int id = resultSet.getInt("reservationId");
            int placeId = resultSet.getInt("placeId");
            Date date = resultSet.getDate("date");
            Time startTime = resultSet.getTime("startTime");
            Time endTime = resultSet.getTime("endTime");
            String clientLogin = resultSet.getString("clientLogin");
            reservations.put(id, new Reservation(id, placeId, clientLogin,
             date.toLocalDate(), startTime.toLocalTime(), endTime.toLocalTime()));
        }
        return reservations;
    }


    /**
     * Получение бронирования по идентификатору.
     * @param id Идентификатор бронирования.
     * @return Бронирование или null, если бронирование не найдено.
     */
    public Reservation getReservation(int id) throws SQLException{
        Reservation reservation = null;
        String request = "SELECT * FROM Reservation WHERE id=?";
        PreparedStatement statement = connection.prepareStatement(request);
        statement.setInt(1, id);
        ResultSet resultSet = statement.executeQuery();
        if(resultSet.next()) {
            int placeId = resultSet.getInt("placeId");
            Date date = resultSet.getDate("date");
            Time startTime = resultSet.getTime("startTime");
            Time endTime = resultSet.getTime("endTime");
            String clientLogin = resultSet.getString("clientLogin");
            reservation = new Reservation(id, placeId, clientLogin,
             date.toLocalDate(), startTime.toLocalTime(), endTime.toLocalTime());
        }
        return reservation;
    }

    /**
     * Добавление нового бронирования.
     * @param placeId Идентификатор места, которое нужно забронировать.
     * @param clientLogin Логин клиента, который делает бронирование.
     * @param date Дата бронирования.
     * @param startTime Время начала бронирования.
     * @param endTime Время окончания бронирования.
     */
    public void addReservation(int placeId, String clientLogin, LocalDate date, 
    LocalTime startTime, LocalTime endTime) throws SQLException{
        String request = "INSERT INTO Reservation (placeId, clientLogin, startTime, endTime, date)"  +
        "VALUES(?, ?, ?, ?, ?)";
        PreparedStatement statement = connection.prepareStatement(request);
        statement.setInt(1, placeId);
        statement.setString(2, clientLogin);
        statement.setTime(3, Time.valueOf(startTime));
        statement.setTime(4, Time.valueOf(endTime));
        statement.setDate(5, Date.valueOf(date));
        statement.executeUpdate();
    }

    /**
     * Удаление бронирований для определенного места.
     * @param placeId Идентификатор места, для которого нужно удалить бронирования.
     */
    public void deleteElementForPlaceId(int placeId) throws SQLException {
        String request = "DELETE FROM Reservation WHERE placeId=?";
        PreparedStatement statement = connection.prepareStatement(request);
        statement.setInt(1, placeId);
        statement.executeQuery();
    }

    /**
     * Обновление времени бронирования.
     * @param id Идентификатор бронирования, время которого нужно обновить.
     * @param startTime Новое время начала бронирования.
     * @param endTime Новое время окончания бронирования.
     */
    public void updateTime(int id, LocalTime startTime, LocalTime endTime) throws SQLException{ 
        String request = "UPDATE Reservation SET startTime=?, endTime=? WHERE id=?";
        PreparedStatement statement = connection.prepareStatement(request);
        statement.setTime(1, Time.valueOf(startTime));
        statement.setTime(2, Time.valueOf(endTime));
        statement.setInt(3, id);
        statement.executeQuery();
    }

    /**
     * Удаление бронирования.
     * @param id Идентификатор бронирования, которое нужно удалить.
     */
    public void delete(int id) throws SQLException{
        String request = "DELETE FROM Reservation WHERE id=?";
        PreparedStatement statement = connection.prepareStatement(request);
        statement.setInt(1, id);
        statement.executeQuery();
    }

    /**
     * Метод получения записей отсортированных по месту.
     * @param Type Тип места.
     * @return Список записей указанного типа.
     */
    public Collection<Reservation> getReservationsForType() throws SQLException { 
        String request = "SELECT * FROM Reservation GROUP BY placeType";
        Statement statement = connection.createStatement();
        return resultForMap(statement.executeQuery(request)).values();
    }

     /**
     * Метод получения записей на конкретный день.
     * @param date Денью
     * @return Список записей на указаный день.
     */
    public Collection<Reservation> getReservationsForDate(LocalDate date) throws SQLException{
        String request = "SELECT * FROM Reservation WHERE date=?";
        PreparedStatement statement  = connection.prepareStatement(request);
        statement.setDate(1, Date.valueOf(date));
        return resultForMap(statement.executeQuery()).values();
    }

    /**
     * Метод получения записей на конкретное место.
     * @param idPlace Идентификатор места.
     * @return Список записей на указаное место.
     */
    public Collection<Reservation> getReservationsForPlace(int idPlace) throws SQLException{ 
        String request = "SELECT * FROM Reservation WHERE placeId=?";
        PreparedStatement statement = connection.prepareStatement(request);
        statement.setInt(1, idPlace);
        return resultForMap(statement.executeQuery()).values();
    }

    /**
     * Метод получения записей одного пользователя.
     * @param login Логин пользователя.
     * @return Список записей указанного пользователя.
     */
    public Collection<Reservation> getReservationsForLogin(String login) throws SQLException { 
        String request = "SELECT * FROM Reservation WHERE clientLogin=?";
        PreparedStatement statement = connection.prepareStatement(request);
        statement.setString(1, login);
        return resultForMap(statement.executeQuery()).values();
    }
    
}
