package com.example.out.dao;

import java.util.ArrayList;
import java.util.List;

import com.example.model.WorkPlace;


public class WorkPlaceDAO {
    private List<WorkPlace> places;

    {
        places = new ArrayList<>();

        places.add(new WorkPlace("user0001"));
        places.add(new WorkPlace("user0001") );
        places.add(new WorkPlace("user0001"));
    }

    public List<WorkPlace> getPlaces() {
        return places;
    }

    public WorkPlace getPlace(int id) {
        return places.stream().filter(place -> place.getId() == id).findAny().orElse(null);
    }

    public void addWorkPlace(String loginOwner) {
        places.add(new WorkPlace(loginOwner));
    }
    public void deletePlace(int id) {
        places.removeIf(p -> p.getId() == id);
    }
}
