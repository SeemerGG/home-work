package com.example.out.dao;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

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
     * @param placeId Идентификатор места, которое нужно забронировать.
     * @param clientLogin Логин клиента, который делает бронирование.
     * @param date Дата бронирования.
     * @param startTime Время начала бронирования.
     * @param endTime Время окончания бронирования.
     */
    public void addReservation(int placeId, String clientLogin, LocalDate date, 
    LocalTime startTime, LocalTime endTime) {
        Reservation reservation = new Reservation(placeId, clientLogin, date, startTime, endTime);
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
            if (entry.getValue().getPlaceId() == placeId) {
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

    
    
}
