package com.example.out.dao;

import java.util.ArrayList;
import java.util.List;

import com.example.model.Place;
import com.example.model.PlaceType;

/**
 * DAO класс для управления местами.
 * Позволяет выполнять операции с коллекцией рабочих мест, такие как получение, добавление и удаление мест.
 */
public class PlaceDAO {

    private List<Place> places;

    {
        places = new ArrayList<>();

        places.add(new Place("user0001"));
        places.add(new Place("user0001"));
        places.add(new Place("user0001") );
        places.add(new Place("user0001", 40));
        places.add(new Place("user0001", 45));
    }

    /**
     * Добавляет новое рабочее место в список.
     * @param loginOwner Логин владельца рабочего места.
     */
    public void addPlace(String loginOwner) {
        places.add(new Place(loginOwner));
    }

    /**
     * Добавляет новый конференц-зал в список.
     * @param loginOwner Логин владельца конференц-зала.
     * @param seats Количество мест в конференц-зале.
     */
    public void addConferenseRoom(String loginOwner, int seats) {
        places.add(new Place(loginOwner, seats));
    }

    /**
     * Получает список всех мест.
     * @return Список мест.
     */
    public List<Place> getPlaces() {
        return places;
    }

    /**
     * Получает список всех рабочих мест.
     * @return Список рабочих мест.
     */
    public List<Place> getWorkPlaces() {
        return places.stream().filter(place -> place.getPlaceType() == PlaceType.WORKPLACE).toList();
    }

    /**
     * Получает список всех конференц-залов.
     * @return Список конференц-залов.
     */
    public List<Place> getPlacesConferenceRoom() {
        return places.stream().filter(place -> place.getPlaceType() == PlaceType.CONFERENCEROOM).toList();
    }

    /**
     * Получает место по его идентификатору.
     * @param id Идентификатор рабочего места.
     * @return Место или null, если место с таким идентификатором не найдено.
     */
    public Place getPlace(int id) {
        return places.stream().filter(place -> place.getId() == id).findAny().orElse(null);
    }

    /**
     * Удаляет место из списка по его идентификатору.
     * @param id Идентификатор места для удаления.
     */
    public void deletePlace(int id) {
        places.removeIf(p -> p.getId() == id);
    }

    /**
     * Проверяет существует ли место с таким идентификатором.
     * @param id Идентификатор места.
     * @return True если место существует иначе False. 
     */
    public boolean exist(int id) {
        return places.stream().anyMatch(place -> place.getId() == id);
    }

    /**
     * Возвращает список мест принадлежащих одному пользователю.
     * @param loginOwner
     * @return Список мест.
     */
    public List<Place> getPlacesOneOwner(String loginOwner) {
        return places.stream().filter(p -> p.getLoginOwner().equals(loginOwner)).toList();
    }
}
