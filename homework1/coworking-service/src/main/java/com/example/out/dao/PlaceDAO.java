package com.example.out.dao;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.example.model.Place;

/**
 * DAO класс для управления местами.
 * Позволяет выполнять операции с коллекцией рабочих мест, такие как получение, добавление и удаление мест.
 */
public final class PlaceDAO {

    public Connection connection;

    /**
     * Конструктор.
     */
    public PlaceDAO(Connection connection) {
        this.connection = connection;
    }

    /**
     * Добавляет новое рабочее место в список.
     * @param loginOwner Логин владельца рабочего места.
     */
    public void addPlace(String loginOwner) throws SQLException{
        String insertDataSQL = "INSERT INTO \"place\" (login_owner, place_type, seats) VALUES (?, 'WORKPLACE', 1)";
        PreparedStatement preparedStatement = connection.prepareStatement(insertDataSQL);
        preparedStatement.setString(1, loginOwner);
        preparedStatement.executeUpdate();
    }

    /**
     * Добавляет новый конференц-зал в список.
     * @param loginOwner Логин владельца конференц-зала.
     * @param seats Количество мест в конференц-зале.
     */
    public void addConferenseRoom(String loginOwner, int seats) throws SQLException{
        String request = "INSERT INTO \"place\" (login_owner, place_type, seats) VALUES (?, 'CONFERENCEROOM', ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(request);
        preparedStatement.setString(1, loginOwner);
        preparedStatement.setInt(2, seats);
        preparedStatement.executeUpdate();
    }

    /**
     * Получает список всех мест.
     * @return Список мест.
     */
    public Map<Integer, Place> getPlaces() throws SQLException{
        String request = "SELECT * FROM \"place\"";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(request);
        return resultForMap(resultSet);
    }

    /**
     * Вспомогательная функция для получение мест в виде Map.
     * @return Список мест.
     */
    private Map<Integer, Place> resultForMap(ResultSet resultSet) throws SQLException{
        Map<Integer, Place> places = new HashMap<>();
        while(resultSet.next()) {
            int id = resultSet.getInt("place_id");
            String placeType = resultSet.getString("place_type");
            String loginOwner = resultSet.getString("login_owner");
            int seats = resultSet.getInt("seats");
            places.put(id, new Place(id, loginOwner, placeType, seats));
        }
        return places;
    }

    /**
     * Получает список всех рабочих мест.
     * @return Список рабочих мест.
     */
    public Map<Integer, Place> getWorkPlaces() throws SQLException{
        String request = "SELECT * FROM \"place\" WHERE place_type='WORKPLACE'";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(request);
        return resultForMap(resultSet);
    }

    /**
     * Получает список всех конференц-залов.
     * @return Список конференц-залов.
     */
    public Map<Integer, Place> getPlacesConferenceRoom() throws SQLException{
        String request = "SELECT * FROM \"place\" WHERE place_type='CONFERENCEROOM'";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(request);
        return resultForMap(resultSet);
    }

    /**
     * Получает место по его идентификатору.
     * @param id Идентификатор рабочего места.
     * @return Место или null, если место с таким идентификатором не найдено.
     */
    public Place getPlace(int id) throws SQLException{
        Place place = null;
        String request = "SELECT * FROM \"place\" WHERE place_id=?";
        PreparedStatement statement = connection.prepareStatement(request);
        statement.setInt(1, id);
        ResultSet resultSet = statement.executeQuery();
        if(resultSet.next()) {
            String placeType = resultSet.getString("place_type");
            String loginOwner = resultSet.getString("login_owner");
            int seats = resultSet.getInt("seats");
            place = new Place(id, placeType, loginOwner, seats);
        }
        return place;
    }

    /**
     * Удаляет место из списка по его идентификатору.
     * @param id Идентификатор места для удаления.
     */
    public void deletePlace(int id) throws SQLException {
        String request = "DELETE FROM \"place\" WHERE place_id=?";
        PreparedStatement statement = connection.prepareStatement(request);
        statement.setInt(1, id);
        statement.executeUpdate();
    }

    /**
     * Проверяет существует ли место с таким идентификатором.
     * @param id Идентификатор места.
     * @return True если место существует иначе False. 
     */
    public boolean exist(int id) throws SQLException{
        String request = "SELECT EXISTS(SELECT 1 FROM \"place\" WHERE place_id=?)";
        PreparedStatement statement = connection.prepareStatement(request);
        statement.setInt(1, id);
        ResultSet resultSet = statement.executeQuery();
        if(resultSet.next()) {
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Возвращает список мест принадлежащих одному пользователю.
     * @param loginOwner
     * @return Список мест.
     */
    public Map<Integer, Place> getPlacesOneOwner(String loginOwner) throws SQLException{
        String request = "SELECT * FROM \"place\" WHERE login_owner=?";
        PreparedStatement statement = connection.prepareStatement(request);
        statement.setString(1, loginOwner);
        return resultForMap(statement.executeQuery());
    }
}
