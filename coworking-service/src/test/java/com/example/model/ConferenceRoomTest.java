package com.example.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ConferenceRoomTest {
    private ConferenceRoom conferenceRoom;
    private final int initialSeats = 10;
    private final String loginOwner = "ownerLogin";

    @BeforeEach
    void setUp() {
        conferenceRoom = new ConferenceRoom(loginOwner, initialSeats);
    }

    @Test
    void testGetSeats() {
        assertEquals(initialSeats, conferenceRoom.getSeats(), "Количество мест должно соответствовать исходному значению.");
    }

    @Test
    void testSetSeats() {
        int newSeats = 20;
        conferenceRoom.setSeats(newSeats);
        assertEquals(newSeats, conferenceRoom.getSeats(), "Метод setSeats должен корректно обновлять количество мест.");
    }

    @Test
    void testToString() {
        String expectedString = loginOwner + " Количество мест: " + initialSeats;
        assertTrue(conferenceRoom.toString().contains(expectedString), "Метод toString должен возвращать строку с логином владельца и количеством мест.");
    }
}
