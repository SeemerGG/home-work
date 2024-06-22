package com.example.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class Reservation {

    private static int lastId = 0;
    private int id;
    private Place place;
    private String clientLogin;
    private LocalTime startTime;
    private LocalTime endTime;
    private LocalDate date;
    private PlaceType placeType;

    public Reservation(Place place, String clientLogin, LocalDate date, 
    LocalTime startTime, LocalTime endTime) {
        if(place.getClass() == WorkPlace.class) {
            placeType = PlaceType.WORKPLACE;
        }
        else {
            placeType = PlaceType.CONFERENCEROOM;
        }
        this.place = place;
        this.clientLogin = clientLogin;
        this.startTime = startTime;
        this.endTime = endTime;
        this.date = date;
        this.id = ++lastId;
    }

    public PlaceType getPlaceType() {
        return placeType;
    }

    public String getClientLogin() {
        return clientLogin;
    }

    public int getId() {
        return id;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Place getPlace() {
        return place;
    }

    public void setTime(LocalTime startTime, LocalTime endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }
  
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ID: ");
        sb.append(id);
        sb.append(" PlaceId: ");
        sb.append(place.getId());
        sb.append(" Тип места: ");
        sb.append(placeType.name());
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
