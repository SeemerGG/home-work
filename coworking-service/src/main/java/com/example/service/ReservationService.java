package com.example.service;

import java.sql.SQLException;
import java.util.Collection;

import com.example.domain.model.Reservation;

/**
 * Интерфейс задающий логику взаимодействия с бронированиями.
 */
public interface ReservationService {

    /**
     * Метод бронирования места.
     * Проверяет доступность места и осуществляет бронирование, если это возможно.
     * @param idPlace Идентификатор места для бронирования.
     * @param starTime Время начала бронирования.
     * @param endTime Время окончания бронирования.
     * @param date День начала бронирования.
     * @param currUserLogin Логин пользователя готового совершить бронирование.
     */
    public boolean reservating(Reservation reservation) throws Exception;

    /**
     * Удаляет бронирование по его идентификатору.
     * Удаляет бронирование из системы и обновляет представление.
     * @param id Идентификатор бронирования для удаления.
     */
    public void deleteReservaton(int id, String login) throws Exception;

    /**
     * Обновляет время бронирования для указанного идентификатора.
     * Проверяет наличие конфликтов с существующими бронированиями и обновляет время, если это возможно.
     * @param id Идентификатор бронирования для обновления.
     * @param startTime Новое время начала бронирования.
     * @param endTime Новое время окончания бронирования.
     */
    public void updateTime(Reservation reservation) throws Exception;

    /**
     * Фильтрует бронирования по типу места.
     * Передает представлению список бронирований, соответствующих указанному типу места, для отображения.
     * @param param Тип места для фильтрации ('conference' или 'work').
     */
    public Collection<Reservation> filterForType(String login) throws SQLException;
    /**
     * Фильтрует бронирования по дате.
     * Сортирует и передает представлению список бронирований пользователя отсортированных по дате.
     */
    public Collection<Reservation> filterForDate(String login) throws SQLException;

    /**
     * Фильтрует бронирования по владельцу места.
     * Сортирует и передает представлению список бронирований пользователя по владельцу места.
     */
    public Collection<Reservation> filterForOwner(String login) throws SQLException;
}
