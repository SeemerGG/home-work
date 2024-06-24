package com.example.out.dao;

import java.util.ArrayList;
import java.util.List;

import com.example.model.ConferenceRoom;

/**
 * DAO класс для управления данными конференц-залов.
 * Позволяет выполнять операции с коллекцией конференц-залов, такие как получение, добавление и удаление.
 */
public class ConferenceRoomDAO {

    private List<ConferenceRoom> places;

    {
        places = new ArrayList<>();

        places.add(new ConferenceRoom("user0001", 40));
        places.add(new ConferenceRoom("user0001", 45));
    }

    /**
     * Получает список всех конференц-залов.
     * @return Список конференц-залов.
     */
    public List<ConferenceRoom> getPlaces() {
        return places;
    }

    /**
     * Получает конференц-зал по его идентификатору.
     * @param id Идентификатор конференц-зала.
     * @return Конференц-зал или null, если зал с таким идентификатором не найден.
     */
    public ConferenceRoom getPlace(int id) {
        return places.stream().filter(place -> place.getId() == id).findAny().orElse(null);
    }

    /**
     * Добавляет новый конференц-зал в список.
     * @param loginOwner Логин владельца конференц-зала.
     * @param seats Количество мест в конференц-зале.
     */
    public void addConferenseRoom(String loginOwner, int seats) {
        places.add(new ConferenceRoom(loginOwner, seats));
    }

    /**
     * Удаляет конференц-зал из списка по идентификатору.
     * @param id Идентификатор конференц-зала для удаления.
     */
    public void deleteElement(int id) {
        places.removeIf(p -> p.getId() == id);
    }
}
