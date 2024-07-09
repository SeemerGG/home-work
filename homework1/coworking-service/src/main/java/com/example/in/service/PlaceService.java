package com.example.in.service;

import java.sql.SQLException;
import java.util.Collection;

import com.example.annotation.Loggable;
import com.example.infrastructure.database.DBSingleton;
import com.example.model.Place;
import com.example.model.PlaceType;
import com.example.out.dao.PlaceDAO;

@Loggable
public class PlaceService {

    private final PlaceDAO placeDAO;
    
    public PlaceService() {
        placeDAO = new PlaceDAO(DBSingleton.getInstance());
    }

    /**
     * Метод ищет все места зарегистрированные на логин текущего пользователя и передает их представлению для вывода.
     */
    public Collection<Place> myPublication(String login) throws SQLException{
        return placeDAO.getPlacesOneOwner(login).values();
    }

    /**
     * Метод удаляет место с указанным id, а также все записи с указаным местом.
     * @param id Идентификационный номер места, которое надо удалить.
     */
    public void deleteMyPlace(int id, String login) throws Exception{
        if(!placeDAO.getPlace(id).getLoginOwner().equals(login)) {
            throw new Exception("Недостаточно прав.");
        }
        placeDAO.deletePlace(id);
    }

    /**
     * Создает новый конференц-зал для текущего пользователя.
     * Добавляет конференц-зал с указанным количеством мест в систему и обновляет представление.
     * @param seats Количество мест в новом конференц-зале.
     */
    public void createMyPlace(int seats, PlaceType placeType, String login) throws SQLException{
        placeDAO.add(login, seats, placeType);
    }

}
