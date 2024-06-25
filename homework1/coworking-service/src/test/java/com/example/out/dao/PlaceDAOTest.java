package com.example.out.dao;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.example.model.Place;

public class PlaceDAOTest {

     private PlaceDAO placeDAO;

    @BeforeEach
    void setUp() {
        placeDAO = new PlaceDAO();
    }

    @Test
    @DisplayName("Добавление нового рабочего места")
    void testAddPlace() {
        String loginOwner = "user0002";
        placeDAO.addPlace(loginOwner);
        Map<Integer, Place> places = placeDAO.getPlaces();
        assertTrue(places.values().stream().anyMatch(place -> place.getLoginOwner().equals(loginOwner)),
                "Новое рабочее место должно быть добавлено");
    }

    @Test
    @DisplayName("Добавление нового конференц-зала")
    void testAddConferenceRoom() {
        String loginOwner = "user0003";
        int seats = 50;
        placeDAO.addConferenseRoom(loginOwner, seats);
        Map<Integer, Place> places = placeDAO.getPlacesConferenceRoom();
        assertTrue(places.values().stream().anyMatch(place -> place.getLoginOwner().equals(loginOwner) && place.getSeats() == seats),
                "Новый конференц-зал должен быть добавлен");
    }

    @Test
    @DisplayName("Получение места по идентификатору")
    void testGetPlace() {
        int id = 1; 
        Place place = placeDAO.getPlace(id);
        assertNotNull(place, "Место с данным идентификатором должно существовать");
    }

    @Test
    @DisplayName("Удаление места по идентификатору")
    void testDeletePlace() {
        int id = 1; 
        assertTrue(placeDAO.exist(id), "Место до удаления должно существовать");
        placeDAO.deletePlace(id);
        assertFalse(placeDAO.exist(id), "Место после удаления не должно существовать");
    }

    @Test
    @DisplayName("Проверка существования места по идентификатору")
    void testExist() {
        int id = 1; 
        assertTrue(placeDAO.exist(id), "Место с данным идентификатором должно существовать");
    }

    @Test
    @DisplayName("Получение списка мест одного владельца")
    void testGetPlacesOneOwner() {
        String loginOwner = "user0001";
        Map<Integer, Place> places = placeDAO.getPlacesOneOwner(loginOwner);
        assertFalse(places.isEmpty(), "Список мест одного владельца не должен быть пустым");
        assertTrue(places.values().stream().allMatch(place -> place.getLoginOwner().equals(loginOwner)),
                "Все места в списке должны принадлежать одному владельцу");
    }
}
