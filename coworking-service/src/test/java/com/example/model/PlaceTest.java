package com.example.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class PlaceTest {
    @Test
    @DisplayName("Проверка корректности создания элемента.")
    void testGetId() {

        Place place = new Place(1, "testOwner") {};
        assertTrue(place.getId() > 0, "ID должен быть положительным числом и уникальным для каждого экземпляра.");
    }

    @Test
    @DisplayName("Проверка корректности логина созданного элемента.")
    void testGetLoginOwner() {

        String loginOwner = "testOwner";
        Place place = new Place(1, loginOwner) {};
        assertEquals(loginOwner, place.getLoginOwner(), "Логин владельца должен соответствовать заданному значению.");
    }

    @Test
    @DisplayName("Проверка корректности строкового представления.")
    void testToString() {
        
        String loginOwner = "testOwner";
        Place place = new Place(1, loginOwner) {};
        String expectedString = "ID: " + 1 + " Логин собственника: " + loginOwner  + " Тип места: Рабочие место";
        assertEquals(expectedString, place.toString(), " Метод toString должен корректно отображать информацию о месте.");
    }
}
