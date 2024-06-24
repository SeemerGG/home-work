package com.example.out.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.example.model.WorkPlace;

public class WorkPlaceDAOTest {
    private WorkPlaceDAO workPlaceDAO;

    @BeforeEach
    void setUp() {
        workPlaceDAO = new WorkPlaceDAO();
        workPlaceDAO.addWorkPlace("owner1");
        workPlaceDAO.addWorkPlace("owner2");
    }

    @Test
    void testGetPlaces() {
        assertFalse(workPlaceDAO.getPlaces().isEmpty(), "Список рабочих мест не должен быть пустым");
    }

    @Test
    void testGetPlace() {
        WorkPlace place = workPlaceDAO.getPlace(1);
        assertNotNull(place, "Рабочее место с id=1 должно существовать");
    }

    @Test
    void testAddWorkPlace() {
        String loginOwner = "newOwner";
        workPlaceDAO.addWorkPlace(loginOwner);
        WorkPlace newPlace = workPlaceDAO.getPlace(6); 
        assertNotNull(newPlace, "Новое рабочее место должно быть добавлено");
        assertEquals(loginOwner, newPlace.getLoginOwner(), "Логин владельца должен соответствовать заданному");
    }

    @Test
    void testDeletePlace() {
        int placeIdToDelete = 1;
        workPlaceDAO.deletePlace(placeIdToDelete);
        WorkPlace deletedPlace = workPlaceDAO.getPlace(placeIdToDelete);
        assertNull(deletedPlace, "Рабочее место с id=1 должно быть удалено");
    }
    
}
