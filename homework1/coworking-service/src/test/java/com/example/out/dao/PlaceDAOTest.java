package com.example.out.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.example.model.Place;
import com.example.model.PlaceType;

public class PlaceDAOTest {

    private PlaceDAO placeDAO;

    @BeforeEach
    void setUp() {
        placeDAO = new PlaceDAO();
    }

    @Test
    void addPlace_ShouldAddPlace() {
        int initialSize = placeDAO.getPlaces().size();
        String loginOwner = "user0002";
        placeDAO.addPlace(loginOwner);
        int newSize = placeDAO.getPlaces().size();
        assertEquals(initialSize + 1, newSize);
        assertEquals(loginOwner, placeDAO.getPlaces().get(newSize - 1).getLoginOwner());
    }

    @Test
    void addConferenseRoom_ShouldAddConferenceRoom() {
        int initialSize = placeDAO.getPlaces().size();
        String loginOwner = "user0003";
        int seats = 50;
        placeDAO.addConferenseRoom(loginOwner, seats);
        int newSize = placeDAO.getPlaces().size();
        Place addedPlace = placeDAO.getPlaces().get(newSize - 1);
        assertEquals(initialSize + 1, newSize);
        assertEquals(loginOwner, addedPlace.getLoginOwner());
        assertEquals(seats, addedPlace.getSeats());
        assertEquals(PlaceType.CONFERENCEROOM, addedPlace.getPlaceType());
    }

    @Test
    void deletePlace_ShouldRemovePlace() {
        int idToDelete = placeDAO.getPlaces().get(0).getId();
        int initialSize = placeDAO.getPlaces().size();

        placeDAO.deletePlace(idToDelete);
        int newSize = placeDAO.getPlaces().size();

        assertEquals(initialSize - 1, newSize);
        assertNull(placeDAO.getPlace(idToDelete));
    }

    @Test
    void exist_ShouldReturnTrueIfExists() {
        int id = placeDAO.getPlaces().get(0).getId();

        boolean exists = placeDAO.exist(id);

        assertTrue(exists);
    }

    @Test
    void getPlacesOneOwner_ShouldReturnPlacesForOneOwner() {
        String loginOwner = "user0001";
        List<Place> expectedPlaces = placeDAO.getPlacesOneOwner(loginOwner);

        List<Place> actualPlaces = placeDAO.getPlacesOneOwner(loginOwner);

        assertEquals(expectedPlaces, actualPlaces);
        assertTrue(actualPlaces.stream().allMatch(place -> place.getLoginOwner().equals(loginOwner)));
    }
}
