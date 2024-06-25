package com.example.out.dao;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import com.example.model.Place;
import com.example.model.PlaceType;

/**
 * DAO класс для управления местами.
 * Позволяет выполнять операции с коллекцией рабочих мест, такие как получение, добавление и удаление мест.
 */
public final class PlaceDAO {

    private Map<Integer, Place> places;

    {
        places = new HashMap<Integer,Place>();
        
        for(int i = 0; i < 3; i++) {
            Place place = new Place("user0001");
            Place place2 = new Place("user0001", 30+i);
            places.put(place.getId(), place);
            places.put(place2.getId(), place2);
        }
    }

    /**
     * Добавляет новое рабочее место в список.
     * @param loginOwner Логин владельца рабочего места.
     */
    public void addPlace(String loginOwner) {
        Place place = new Place(loginOwner);
        places.put(place.getId(), place);
    }

    /**
     * Добавляет новый конференц-зал в список.
     * @param loginOwner Логин владельца конференц-зала.
     * @param seats Количество мест в конференц-зале.
     */
    public void addConferenseRoom(String loginOwner, int seats) {
        Place place = new Place(loginOwner, seats);
        places.put(place.getId(), place);
    }

    /**
     * Получает список всех мест.
     * @return Список мест.
     */
    public Map<Integer, Place> getPlaces() {
        return places;
    }

    /**
     * Получает список всех рабочих мест.
     * @return Список рабочих мест.
     */
    public Map<Integer, Place> getWorkPlaces() {
        return places.entrySet().stream().filter(entry -> entry.getValue().getPlaceType() == PlaceType.WORKPLACE)
        .collect(Collectors.toMap(Map.Entry<Integer, Place>::getKey, Map.Entry<Integer, Place>::getValue));
    }

    /**
     * Получает список всех конференц-залов.
     * @return Список конференц-залов.
     */
    public Map<Integer, Place> getPlacesConferenceRoom() {
        return places.entrySet().stream().filter(entry -> entry.getValue().getPlaceType() == PlaceType.CONFERENCEROOM)
        .collect(Collectors.toMap(Map.Entry<Integer, Place>::getKey, Map.Entry<Integer, Place>::getValue));
    }

    /**
     * Получает место по его идентификатору.
     * @param id Идентификатор рабочего места.
     * @return Место или null, если место с таким идентификатором не найдено.
     */
    public Place getPlace(int id) {
        return places.get(id);
    }

    /**
     * Удаляет место из списка по его идентификатору.
     * @param id Идентификатор места для удаления.
     */
    public void deletePlace(int id) {
        places.remove(id);
    }

    /**
     * Проверяет существует ли место с таким идентификатором.
     * @param id Идентификатор места.
     * @return True если место существует иначе False. 
     */
    public boolean exist(int id) {
        return (places.get(id) != null) ?  true : false;
    }

    /**
     * Возвращает список мест принадлежащих одному пользователю.
     * @param loginOwner
     * @return Список мест.
     */
    public Map<Integer, Place> getPlacesOneOwner(String loginOwner) {
        return places.entrySet().stream().filter(p -> p.getValue().getLoginOwner().equals(loginOwner))
        .collect(Collectors.toMap(Map.Entry<Integer, Place>::getKey, Map.Entry<Integer, Place>::getValue));
    }
}
