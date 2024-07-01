package com.example.model;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Класс, представляющий бронирование места.
 * Хранит информацию о бронировании, включая место, клиента, время и дату.
 */
public class Reservation {

    private int id;
    private int placeId;
    private String clientLogin;
    private LocalTime startTime;
    private LocalTime endTime;
    private LocalDate date;

    /**
     * Конструктор для создания бронирования.
     * Определяет тип места и инициализирует бронирование с уникальным ID.
     * @param id Идентификатор брони.
     * @param placeId Идентификатор места.
     * @param clientLogin Логин клиента.
     * @param date Дата бронирования.
     * @param startTime Время начала бронирования.
     * @param endTime Время окончания бронирования.
     */
    public Reservation(int id, int placeId, String clientLogin, LocalDate date, 
    LocalTime startTime, LocalTime endTime) {
        this.placeId = placeId;
        this.clientLogin = clientLogin;
        this.startTime = startTime;
        this.endTime = endTime;
        this.date = date;
        this.id = id;
    }

    /**
     * Получает логин клиента, осуществляющего бронирование.
     * @return Логин клиента.
     */
    public String getClientLogin() {
        return clientLogin;
    }

    /**
     * Получает уникальный идентификатор бронирования.
     * @return ID бронирования.
     */
    public int getId() {
        return id;
    }

     /**
     * Получает время начала бронирования.
     * @return Время начала.
     */
    public LocalTime getStartTime() {
        return startTime;
    }

     /**
     * Устанавливает время начала бронирования.
     * @param startTime Новое время начала.
     */
    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

     /**
     * Получает время окончания бронирования.
     * @return Время окончания.
     */
    public LocalTime getEndTime() {
        return endTime;
    }

     /**
     * Устанавливает время окончания бронирования.
     * @param endTime Новое время окончания.
     */
    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    /**
     * Получает дату бронирования.
     * @return Дата бронирования.
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * Устанавливает дату бронирования.
     * @param date Новая дата бронирования.
     */
    public void setDate(LocalDate date) {
        this.date = date;
    }

    /**
     * Получает идентификатор места бронирования.
     * @return Идентификатор места.
     */
    public int getPlaceId() {
        return placeId;
    }

    /**
     * Устанавливает время начала и окончания бронирования.
     * @param startTime Новое время начала.
     * @param endTime Новое время окончания.
     */
    public void setTime(LocalTime startTime, LocalTime endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }
  
    /**
     * Возвращает строковое представление объекта бронирования.
     * @return Строковое представление бронирования.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ID: ");
        sb.append(id);
        sb.append(" PlaceId: ");
        sb.append(placeId);
        sb.append(" Логин клиента: ");
        sb.append(clientLogin);
        sb.append(" День:");
        sb.append(date);
        sb.append(" Время начала: ");
        sb.append(startTime);
        sb.append(" Вермя конца: ");
        sb.append(endTime);
        return sb.toString();
    }

}
