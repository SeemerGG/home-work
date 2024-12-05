package com.example.service;

import java.sql.SQLException;
import java.util.Collection;

import com.example.domain.model.Place;

/**
 * Интерфейс задающий логику взаимодействия с местами.
 */
public interface PlaceService {

    /**
     * Метод ищет все места зарегистрированные на логин текущего пользователя и передает их представлению для вывода.
     */
    public Collection<Place> myPublication(String login) throws SQLException;

    /**
     * Метод удаляет место с указанным id, а также все записи с указаным местом.
     * @param id Идентификационный номер места, которое надо удалить.
     */
    public void deleteMyPlace(int id, String login) throws Exception;

    /**
     * Создает новый конференц-зал для текущего пользователя.
     * Добавляет конференц-зал с указанным количеством мест в систему и обновляет представление.
     * @param seats Количество мест в новом конференц-зале.
     */
    public void createMyPlace(Place place) throws SQLException;

}
