package com.example.in.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import com.example.infrastructure.database.DBSingleton;
import com.example.model.Reservation;
import com.example.out.dao.PlaceDAO;
import com.example.out.dao.ReservationDAO;
import com.example.out.dao.UserDAO;

public class ReservationService {

    private final UserDAO userDAO;
    private final PlaceDAO placeDAO;
    private final ReservationDAO reservationDAO;


    public ReservationService() {
        this.userDAO = new UserDAO(DBSingleton.getInstance());
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
}
