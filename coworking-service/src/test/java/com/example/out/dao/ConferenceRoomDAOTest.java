package com.example.out.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.example.model.ConferenceRoom;

public class ConferenceRoomDAOTest {
    private ConferenceRoomDAO dao;

    @BeforeEach
    void setUp() {
        dao = new ConferenceRoomDAO();
    }

    @Test
    void testGetPlaces() {
        List<ConferenceRoom> places = dao.getPlaces();
        assertFalse(places.isEmpty(), "Список мест не должен быть пустым.");
    }

    @Test
    void testGetPlace() {
        ConferenceRoom place = dao.getPlace(1);
        assertNotNull(place, "Должен вернуться конференц-зал с заданным ID.");
    }

    @Test
    void testAddConferenseRoom() {
        int initialSize = dao.getPlaces().size();
        dao.addConferenseRoom("newUser", 50);
        assertEquals(initialSize + 1, dao.getPlaces().size(), "После добавления размер списка должен увеличиться.");
    }

    @Test
    void testDeleteElement() {
        dao.addConferenseRoom("tempUser", 100); 
        int addedId = dao.getPlaces().get(dao.getPlaces().size() - 1).getId();
        dao.deleteElement(addedId);
        assertNull(dao.getPlace(addedId), "Элемент с заданным ID должен быть удален.");
    }
}
