package com.example.model;


public class ConferenceRoom extends Place{

    private int seats;
    
    public ConferenceRoom(String loginOwner, int seats) {
        super(loginOwner);
        this.seats = seats;
    }
    
    @Override
    public String toString() {
        return super.toString()+" Количество мест: "+seats;
    }

    public int getSeats() {
        return seats;
    }

    public void setSeats(int seats) {
        this.seats = seats;
    }
}
