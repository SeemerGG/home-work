package com.example.out.dao;

import java.util.ArrayList;
import java.util.List;

import com.example.model.ConferenceRoom;

public class ConferenceRoomDAO {

    private List<ConferenceRoom> places;

    {
        places = new ArrayList<>();

        places.add(new ConferenceRoom("user0001", 40));
        places.add(new ConferenceRoom("user0001", 45));
    }

    public List<ConferenceRoom> getPlaces() {
        return places;
    }

    public ConferenceRoom getPlace(int id) {
        return places.stream().filter(place -> place.getId() == id).findAny().orElse(null);
    }

    public void addConferenseRoom(String loginOwner, int seats) {
        places.add(new ConferenceRoom(loginOwner, seats));
    }

    public void deleteElement(int id) {
        places.removeIf(p -> p.getId() == id);
    }
}
