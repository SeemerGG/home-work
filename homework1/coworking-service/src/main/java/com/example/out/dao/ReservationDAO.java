package com.example.out.dao;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


import java.sql.Date;
import java.sql.Time;

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
    public ReservationDAO(Connection connection) {
        this.connection = connection;
    }

    /**
     * Получение списка всех бронирований.
     * @return Список бронирований.
     */
    public Map<Integer, Reservation> getReservations() throws SQLException{
        String request = "SELECT * FROM \"reservation\"";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(request);
        return resultForMap(resultSet);
    }

    private Map<Integer, Reservation> resultForMap(ResultSet resultSet) throws SQLException {
        Map<Integer, Reservation> reservations = new HashMap<>();
        while(resultSet.next()) {
            Reservation reservation = mappingResultSetToReservation(resultSet);
            reservations.put(reservation.getId(), reservation);
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
        String request = "SELECT * FROM \"reservation\" WHERE reservation_id=?";
        PreparedStatement statement = connection.prepareStatement(request);
        statement.setInt(1, id);
        ResultSet resultSet = statement.executeQuery();
        if(resultSet.next()) {
            reservation = mappingResultSetToReservation(resultSet);
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
        String request = "INSERT INTO \"reservation\" (place_id, client_login, start_time, end_time, date)"  +
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
        String request = "DELETE FROM \"reservation\" WHERE place_id=?";
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
        String request = "UPDATE \"reservation\" SET start_time=?, end_time=? WHERE reservation_id=?";
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
        String request = "DELETE FROM \"reservation\" WHERE reservation_id=?";
        PreparedStatement statement = connection.prepareStatement(request);
        statement.setInt(1, id);
        statement.executeQuery();
    }

    /**
     * Метод получения записей отсортированных по месту.
     * @param login Логин пользователя зарегистрировавшего запись.
     * @return Список записей указанного типа.
     */
    public Collection<Reservation> getOrderedReservationByType(String login) throws SQLException { 
        String request = "SELECT * FROM \"reservation\" JOIN \"place\" ON \"reservation\".place_id=\"place\".place_id WHERE client_login=? ORDER BY place_type";
        PreparedStatement statement = connection.prepareStatement(request);
        statement.setString(1, login);
        return resultForMap(statement.executeQuery()).values();
    }

    /**
     * Метод получения записей отсортированных по дате.
     * @return Список записей указанного типа.
     */
    public Collection<Reservation> getOrderedReservationByDay(String login) throws SQLException { 
        String request = "SELECT * FROM \"reservation\" WHERE client_login=? ORDER BY date";
        PreparedStatement statement = connection.prepareStatement(request);
        statement.setString(1, login);
        return resultForMap(statement.executeQuery()).values();
    }

    /**
     * Метод получения записей отсортированных по владельцу места.
     * @param login Логин пользователя зарегистрировавшего запись.
     * @return Список записей указанного типа.
     */
    public Collection<Reservation> getOrderedReservationByOwner(String login) throws SQLException { 
        String request = "SELECT * FROM \"reservation\" JOIN \"place\" ON \"reservation\".place_id=\"place\".place_id WHERE client_login=? ORDER BY login_owner";
        PreparedStatement statement = connection.prepareStatement(request);
        statement.setString(1, login);
        return resultForMap(statement.executeQuery()).values();
    }

     /**
     * Метод получения записей на конкретный день.
     * @param date Денью
     * @return Список записей на указаный день.
     */
    public Collection<Reservation> getReservationsForDate(LocalDate date) throws SQLException{
        String request = "SELECT * FROM \"reservation\" WHERE date=?";
        PreparedStatement statement  = connection.prepareStatement(request);
        statement.setDate(1, Date.valueOf(date));
        ResultSet resultSet = statement.executeQuery();
        Collection<Reservation> reservs = new ArrayList<>();
        while(resultSet.next()) {
            Reservation reservation = mappingResultSetToReservation(resultSet);
            reservs.add(reservation);
        }
        return reservs;
    }

    /**
     * Метод получения записей на конкретное место.
     * @param idPlace Идентификатор места.
     * @return Список записей на указаное место.
     */
    public Collection<Reservation> getReservationsForPlace(int idPlace) throws SQLException{ 
        String request = "SELECT * FROM \"reservation\" WHERE place_id=? ";
        PreparedStatement statement = connection.prepareStatement(request);
        statement.setInt(1, idPlace);
        ResultSet resultSet = statement.executeQuery();
        Collection<Reservation> reservs = new ArrayList<>();
        while(resultSet.next()) {
            Reservation reservation = mappingResultSetToReservation(resultSet);
            reservs.add(reservation);
        }
        return reservs;
    }

    /**
     * Метод получения записей одного пользователя.
     * @param login Логин пользователя.
     * @return Список записей указанного пользователя.
     */
    public Collection<Reservation> getReservationsForLogin(String login) throws SQLException { 
        String request = "SELECT * FROM \"reservation\" WHERE client_login=?";
        PreparedStatement statement = connection.prepareStatement(request);
        statement.setString(1, login);
        ResultSet resultSet = statement.executeQuery();
        Collection<Reservation> reservs = new ArrayList<>();
        while(resultSet.next()) {
            Reservation reservation = mappingResultSetToReservation(resultSet);
            reservs.add(reservation);
        }
        return reservs;
    }

    /**
     * Метод получения записей с определенным местом и датой.
     * @param idPlace Идентификатор места.
     * @param date Дата на которую произведена запись.
     * @return Список записей указанного пользователя.
     */
    public Collection<Reservation> getReservationsForDateAndPlace(int idPlace, LocalDate date) throws SQLException { 
        String request = "SELECT * FROM \"reservation\" WHERE place_id=? AND date=?";
        PreparedStatement statement = connection.prepareStatement(request);
        statement.setInt(1, idPlace);
        statement.setDate(2, Date.valueOf(date));
        ResultSet resultSet = statement.executeQuery();
        Collection<Reservation> reservs = new ArrayList<>();
        while(resultSet.next()) {
            Reservation reservation = mappingResultSetToReservation(resultSet);
            reservs.add(reservation);
        }
        return reservs;
    }
    /**
     * Вспомогательный метод мапинга объекта ResultSet.
     * @param resultSet
     * @return
     * @throws SQLException
     */
    private Reservation mappingResultSetToReservation(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("reservation_id");
        int placeId = resultSet.getInt("place_id");
        Date date = resultSet.getDate("date");
        Time startTime = resultSet.getTime("start_time");
        Time endTime = resultSet.getTime("end_time");
        String clientLogin = resultSet.getString("client_login");
        return new Reservation(id, placeId, clientLogin, date.toLocalDate(), startTime.toLocalTime(), endTime.toLocalTime());
    }
    
}
