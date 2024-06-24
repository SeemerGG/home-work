package com.example.out.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.LocalTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.example.model.ConferenceRoom;
import com.example.model.Place;
import com.example.model.Reservation;

public class ReservationDAOTest {
    private ReservationDAO dao;
    private Place testPlace;
    private String clientLogin;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;

    @BeforeEach
    void setUp() {
        dao = new ReservationDAO();
        testPlace = new ConferenceRoom("ownerLogin", 100);
        clientLogin = "testClient";
        date = LocalDate.now();
        startTime = LocalTime.of(10, 0);
        endTime = LocalTime.of(11, 0);
        dao.addReservation(testPlace, clientLogin, date, startTime, endTime);
    }

    @Test
    void testGetReservations() {
        assertFalse(dao.getReservations().isEmpty(), "Список бронирований не должен быть пустым.");
        System.out.println(dao.getReservations().get(0).toString());
    }

    @Test
    void testGetReservation() {
        Reservation reservation = dao.getReservation(1);
        assertNotNull(reservation, "Должно вернуться бронирование с заданным ID.");
    }

    @Test
    void testAddReservation() {
        int initialSize = dao.getReservations().size();
        dao.addReservation(testPlace, "newClient", date, startTime, endTime);
        assertEquals(initialSize + 1, dao.getReservations().size(), "После добавления размер списка должен увеличиться.");
    }

    @Test
    void testDeleteElementForPlaceId() {
        dao.deleteElementForPlaceId(testPlace.getId());
        assertTrue(dao.getReservations().stream().noneMatch(res -> res.getPlace().getId() == testPlace.getId()), "Все бронирования для данного места должны быть удалены.");
    }

    @Test
    void testUpdateTime() {
        LocalTime newStartTime = LocalTime.of(12, 0);
        LocalTime newEndTime = LocalTime.of(13, 0);
        dao.updateTime(1, newStartTime, newEndTime);
        Reservation updatedReservation = dao.getReservation(1);
        assertEquals(newStartTime, updatedReservation.getStartTime(), "Время начала должно быть обновлено.");
        assertEquals(newEndTime, updatedReservation.getEndTime(), "Время окончания должно быть обновлено.");
    }

    @Test
    void testDelete() {
        dao.delete(1);
        assertNull(dao.getReservation(1), "Бронирование с заданным ID должно быть удалено.");
    }
}
