package com.example.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class PlaceTest {
    @Test
    void testGetId() {
        Place place = new Place("testOwner") {};
        assertTrue(place.getId() > 0, "ID должен быть положительным числом и уникальным для каждого экземпляра.");
    }

    @Test
    void testGetLoginOwner() {
        String loginOwner = "testOwner";
        Place place = new Place(loginOwner) {};
        assertEquals(loginOwner, place.getLoginOwner(), "Логин владельца должен соответствовать заданному значению.");
    }

    @Test
    void testToString() {
        String loginOwner = "testOwner";
        Place place = new Place(loginOwner) {};
        String expectedString = "ID: " + place.getId() + "Логин собственника: " + loginOwner;
        assertEquals(expectedString, place.toString(), "Метод toString должен корректно отображать информацию о месте.");
    }
}
