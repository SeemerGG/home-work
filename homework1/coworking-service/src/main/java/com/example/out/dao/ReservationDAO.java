package com.example.out.dao;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.example.model.Place;
import com.example.model.PlaceType;
import com.example.model.Reservation;

/**
 * Класс для доступа к данным о бронированиях.
 * Позволяет управлять бронированиями мест в системе.
 */
public final class ReservationDAO {

    private Map<Integer, Reservation> list;

    {
        list = new HashMap<Integer, Reservation>();
    }

    /**
     * Получение списка всех бронирований.
     * @return Список бронирований.
     */
    public Map<Integer, Reservation> getReservations() {
        return list;
    }

    /**
     * Получение бронирования по идентификатору.
     * @param id Идентификатор бронирования.
     * @return Бронирование или null, если бронирование не найдено.
     */
    public Reservation getReservation(int id) {
        return list.get(id);
    }

    /**
     * Установка нового списка бронирований.
     * @param list Новый список бронирований.
     */
    public void setList(Map<Integer, Reservation> list) {
        this.list = list;
    }

    /**
     * Добавление нового бронирования.
     * @param place Место, которое нужно забронировать.
     * @param clientLogin Логин клиента, который делает бронирование.
     * @param date Дата бронирования.
     * @param startTime Время начала бронирования.
     * @param endTime Время окончания бронирования.
     */
    public void addReservation(Place place, String clientLogin, LocalDate date, 
    LocalTime startTime, LocalTime endTime) {
        Reservation reservation = new Reservation(place, clientLogin, date, startTime, endTime);
        list.put(reservation.getId(), reservation);
    }

    /**
     * Удаление бронирований для определенного места.
     * @param placeId Идентификатор места, для которого нужно удалить бронирования.
     */
    public void deleteElementForPlaceId(int placeId) {
        Iterator<Map.Entry<Integer, Reservation>> iterator = list.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Integer, Reservation> entry = iterator.next();
            if (entry.getValue().getPlace().getId() == placeId) {
                iterator.remove(); 
            }
        }
    }

    /**
     * Обновление времени бронирования.
     * @param id Идентификатор бронирования, время которого нужно обновить.
     * @param startTime Новое время начала бронирования.
     * @param endTime Новое время окончания бронирования.
     */
    public void updateTime(int id, LocalTime startTime, LocalTime endTime) { 
        Reservation reservation = list.get(id);
        reservation.setTime(startTime, endTime);
        list.remove(id);
        list.put(id, reservation);
    }

    /**
     * Удаление бронирования.
     * @param id Идентификатор бронирования, которое нужно удалить.
     */
    public void delete(int id) {
        list.remove(id);
    }

    /**
     * Метод получения записей на конкретный день.
     * @param date Денью
     * @return Список записей на указаный день.
     */
    public List<Reservation> getReservationsForDate(LocalDate date) {
        return list.values().stream().filter(res -> res.getDate().isEqual(date)).toList();
    }

    /**
     * Метод получения записей на конкретное место.
     * @param idPlace Идентификатор места.
     * @return Список записей на указаное место.
     */
    public List<Reservation> getReservationsForPlace(int idPlace) { 
        return list.values().stream().filter(res -> res.getPlace().getId() == idPlace).toList();
    }

    /**
     * Метод получения записей одного пользователя.
     * @param login Логин пользователя.
     * @return Список записей указанного пользователя.
     */
    public List<Reservation> getReservationsForLogin(String login) { 
        return list.values().stream().filter(res -> res.getClientLogin().equals(login)).toList();
    }

    /**
     * Метод получения записей по указанному типу места.
     * @param Type Тип места.
     * @return Список записей указанного типа.
     */
    public List<Reservation> getReservationsForType(PlaceType Type) { 
        if(Type == PlaceType.CONFERENCEROOM) {
            return list.values().stream().filter(res -> res.getPlace().getPlaceType() == PlaceType.CONFERENCEROOM).toList();
        }
        if(Type == PlaceType.WORKPLACE) {
            return list.values().stream().filter(res -> res.getPlace().getPlaceType() == PlaceType.WORKPLACE).toList();
        }
        return null;
    }
    
}
