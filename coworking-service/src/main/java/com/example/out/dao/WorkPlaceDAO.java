package com.example.out.dao;

import java.util.ArrayList;
import java.util.List;

import com.example.model.WorkPlace;

/**
 * DAO класс для управления рабочими местами.
 * Позволяет выполнять операции с коллекцией рабочих мест, такие как получение, добавление и удаление мест.
 */
public class WorkPlaceDAO {
    private List<WorkPlace> places;

    {
        places = new ArrayList<>();

        places.add(new WorkPlace("user0001"));
        places.add(new WorkPlace("user0001") );
        places.add(new WorkPlace("user0001"));
    }

    /**
     * Получает список всех рабочих мест.
     * @return Список рабочих мест.
     */
    public List<WorkPlace> getPlaces() {
        return places;
    }

    /**
     * Получает рабочее место по его идентификатору.
     * @param id Идентификатор рабочего места.
     * @return Рабочее место или null, если место с таким идентификатором не найдено.
     */
    public WorkPlace getPlace(int id) {
        return places.stream().filter(place -> place.getId() == id).findAny().orElse(null);
    }

    /**
     * Добавляет новое рабочее место в список.
     * @param loginOwner Логин владельца рабочего места.
     */
    public void addWorkPlace(String loginOwner) {
        places.add(new WorkPlace(loginOwner));
    }
    
    /**
     * Удаляет рабочее место из списка по его идентификатору.
     * @param id Идентификатор рабочего места для удаления.
     */
    public void deletePlace(int id) {
        places.removeIf(p -> p.getId() == id);
    }
}
