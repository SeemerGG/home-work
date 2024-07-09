package com.example.in.service;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.example.infrastructure.database.DBSingleton;
import com.example.model.Reservation;
import com.example.out.dao.PlaceDAO;
import com.example.out.dao.ReservationDAO;

public class ReservationService {

    private final PlaceDAO placeDAO;
    private final ReservationDAO reservationDAO;


    public ReservationService() {
        this.placeDAO = new PlaceDAO(DBSingleton.getInstance());
        this.reservationDAO = new ReservationDAO(DBSingleton.getInstance());
    }

    /**
     * Метод бронирования места.
     * Проверяет доступность места и осуществляет бронирование, если это возможно.
     * @param idPlace Идентификатор места для бронирования.
     * @param starTime Время начала бронирования.
     * @param endTime Время окончания бронирования.
     * @param date День начала бронирования.
     * @param currUserLogin Логин пользователя готового совершить бронирование.
     */
    public boolean reservating(Integer idPlace, LocalTime starTime, LocalTime endTime, LocalDate date, String currUserLogin) throws Exception{
        List<Reservation> reservationByPlace = new ArrayList<>(reservationDAO.getReservationsForDateAndPlace(idPlace, date)); 
        if(!reservationByPlace.isEmpty()) {
            for(Reservation reserv : reservationByPlace) {
                if(!reserv.getStartTime().isAfter(starTime) && 
                !reserv.getEndTime().isAfter(endTime)) {
                    throw new Exception("Указанный период уже занят (полностью или частично)!");
                }
                else {
                    reservationDAO.addReservation(idPlace, currUserLogin, date, starTime, endTime);
                    return true;
                }
            }
        }
        else if(placeDAO.exist(idPlace)){
            reservationDAO.addReservation(idPlace, currUserLogin, date, starTime, endTime); 
            return true;
        }
        else {
            throw new Exception("Неверно указан ID места!");
        }
        return false;
    }

    /**
     * Удаляет бронирование по его идентификатору.
     * Удаляет бронирование из системы и обновляет представление.
     * @param id Идентификатор бронирования для удаления.
     */
    public void deleteReservaton(int id, String login) throws Exception{
        if(reservationDAO.getReservation(id).getClientLogin().equals(login)) {
            reservationDAO.delete(id);
        }
        else {
            throw new Exception("Недостаточно прав.");
        } 
    }

    /**
     * Обновляет время бронирования для указанного идентификатора.
     * Проверяет наличие конфликтов с существующими бронированиями и обновляет время, если это возможно.
     * @param id Идентификатор бронирования для обновления.
     * @param startTime Новое время начала бронирования.
     * @param endTime Новое время окончания бронирования.
     */
    public void updateTime(int id, LocalTime startTime, LocalTime endTime, String login) throws Exception{
        Reservation reservation = reservationDAO.getReservation(id);
        if(!reservation.getClientLogin().equals(login)) {
            throw new Exception("Не достаточно прав.");
        }
        List<Reservation> listReserv = new ArrayList<>(reservationDAO.getReservationsForDateAndPlace(id, reservation.getDate()));
        listReserv.removeIf(res -> res.getId() == id);
        for(Reservation reserv : listReserv) {
            if(!reserv.getStartTime().isAfter(startTime) && 
            !reserv.getEndTime().isAfter(endTime)) {
                throw new Exception("Указанное время заняно.");
            }
            else {
                reservationDAO.updateTime(id, startTime, endTime);
            }
        }
    }

    /**
     * Фильтрует бронирования по типу места.
     * Передает представлению список бронирований, соответствующих указанному типу места, для отображения.
     * @param param Тип места для фильтрации ('conference' или 'work').
     */
    public Collection<Reservation> filterForType(String login) throws SQLException {
        return reservationDAO.getOrderedReservationByType(login);
    }

    /**
     * Фильтрует бронирования по дате.
     * Сортирует и передает представлению список бронирований пользователя отсортированных по дате.
     */
    public Collection<Reservation> filterForDate(String login) throws SQLException{
        return reservationDAO.getOrderedReservationByDay(login);
    }

    /**
     * Фильтрует бронирования по владельцу места.
     * Сортирует и передает представлению список бронирований пользователя по владельцу места.
     */
    public Collection<Reservation> filterForOwner(String login) throws SQLException{
        return reservationDAO.getOrderedReservationByOwner(login);
    }
}
