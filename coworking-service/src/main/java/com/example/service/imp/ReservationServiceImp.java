package com.example.service.imp;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.annotation.Loggable;
import com.example.domain.model.Reservation;
import com.example.out.dao.PlaceDAO;
import com.example.out.dao.ReservationDAO;
import com.example.service.ReservationService;

@Service
@Loggable
/**
 * Класс реализующий интерфейс ReservationService.
 */
public class ReservationServiceImp implements ReservationService{

    private final PlaceDAO placeDAO;
    private final ReservationDAO reservationDAO;


    @Autowired
    public ReservationServiceImp(PlaceDAO placeDAO, ReservationDAO reservationDAO) {

        this.placeDAO = placeDAO;
        this.reservationDAO = reservationDAO;
    }

    @Override
    public boolean reservating(Reservation res) throws Exception {

        List<Reservation> reservationByPlace = new ArrayList<>(reservationDAO.getReservationsForDateAndPlace(res.getPlaceId(), res.getDate())); 
        if(!reservationByPlace.isEmpty()) {
            for(Reservation reserv : reservationByPlace) {
                if(!reserv.getStartTime().isAfter(res.getStartTime()) && 
                !reserv.getEndTime().isAfter(res.getEndTime())) {
                    throw new Exception("Указанный период уже занят (полностью или частично)!");
                }
                else {
                    reservationDAO.addReservation(res.getPlaceId(), res.getClientLogin(), res.getDate(), res.getStartTime(), res.getEndTime());
                    return true;
                }
            }
        }
        else if(placeDAO.exist(res.getPlaceId())){
            reservationDAO.addReservation(res.getPlaceId(), res.getClientLogin(), res.getDate(), res.getStartTime(), res.getEndTime()); 
            return true;
        }
        else {
            throw new Exception("Неверно указан ID места!");
        }
        return false;
    }

    @Override
    public void deleteReservaton(int id, String login) throws Exception {

        if(reservationDAO.getReservation(id).getClientLogin().equals(login)) {
            reservationDAO.delete(id);
        }
        else {
            throw new Exception("Недостаточно прав.");
        } 
    }

    @Override
    public void updateTime(Reservation reserv) throws Exception {

        Reservation reservation = reservationDAO.getReservation(reserv.getId());
        if(!reservation.getClientLogin().equals(reserv.getClientLogin())) {
            throw new Exception("Не достаточно прав.");
        }
        List<Reservation> listReserv = new ArrayList<>(reservationDAO.getReservationsForDateAndPlace(reserv.getId(), reservation.getDate()));
        listReserv.removeIf(res -> res.getId() == reserv.getId());
        for(Reservation r : listReserv) {
            if(!r.getStartTime().isAfter(reserv.getStartTime()) && 
            !r.getEndTime().isAfter(reserv.getEndTime())) {
                throw new Exception("Указанное время заняно.");
            }
            else {
                reservationDAO.updateTime(reserv.getId(), reserv.getStartTime(), reserv.getEndTime());
            }
        }
    }

    @Override
    public Collection<Reservation> filterForType(String login) throws SQLException {

        return reservationDAO.getOrderedReservationByType(login);
    }

    @Override
    public Collection<Reservation> filterForDate(String login) throws SQLException {

        return reservationDAO.getOrderedReservationByDay(login);
    }

    @Override
    public Collection<Reservation> filterForOwner(String login) throws SQLException {
        
        return reservationDAO.getOrderedReservationByOwner(login);
    }
}

