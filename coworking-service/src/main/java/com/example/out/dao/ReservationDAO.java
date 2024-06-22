package com.example.out.dao;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import com.example.model.Place;
import com.example.model.Reservation;

public class ReservationDAO {

    private List<Reservation> list;

    {
        list = new ArrayList<>();
    }

    public List<Reservation> getReservations() {
        return list;
    }

    public Reservation getReservation(int id) {
        return list.stream().filter(el -> el.getId() == id).findAny().orElse(null);
    }

    public void setList(List<Reservation> list) {
        this.list = list;
    }

    public void addReservation(Place place, String clientLogin, LocalDate date, 
    LocalTime startTime, LocalTime endTime) {
        list.add(new Reservation(place, clientLogin, date, startTime, endTime));
    }

    public void deleteElementForPlaceId(int placeId) {
        list.removeIf(p -> p.getPlace().getId() == placeId);
    }

    public void updateTime(int id, LocalTime startTime, LocalTime endTime) { 
        getReservation(id).setTime(startTime, endTime);
    }

    public void delete(int id) {
        list.removeIf(res -> res.getId() == id);
    }
}
