package com.example.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.LocalTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ReservationTest {
    private Reservation reservation;
    private Place place;
    private final String clientLogin = "clientLogin";
    private final LocalDate date = LocalDate.of(2024, 6, 23);
    private final LocalTime startTime = LocalTime.of(14, 0);
    private final LocalTime endTime = LocalTime.of(16, 0);

    @BeforeEach
    void setUp() {
        place = new ConferenceRoom("ownerLogin", 10) {};
        reservation = new Reservation(place, clientLogin, date, startTime, endTime);
    }

    @Test
    void testGetPlaceType() {
        assertEquals(PlaceType.CONFERENCEROOM, reservation.getPlaceType(), "Тип места должен соответствовать типу объекта Place.");
    }

    @Test
    void testGetClientLogin() {
        assertEquals(clientLogin, reservation.getClientLogin(), "Логин клиента должен соответствовать заданному значению.");
    }

    @Test
    void testGetId() {
        assertTrue(reservation.getId() > 0, "ID бронирования должен быть положительным числом.");
    }

    @Test
    void testGetStartTime() {
        assertEquals(startTime, reservation.getStartTime(), "Время начала должно соответствовать заданному значению.");
    }

    @Test
    void testSetStartTime() {
        LocalTime newStartTime = LocalTime.of(15, 0);
        reservation.setStartTime(newStartTime);
        assertEquals(newStartTime, reservation.getStartTime(), "Метод setStartTime должен корректно обновлять время начала.");
    }

    @Test
    void testGetEndTime() {
        assertEquals(endTime, reservation.getEndTime(), "Время окончания должно соответствовать заданному значению.");
    }

    @Test
    void testSetEndTime() {
        LocalTime newEndTime = LocalTime.of(17, 0);
        reservation.setEndTime(newEndTime);
        assertEquals(newEndTime, reservation.getEndTime(), "Метод setEndTime должен корректно обновлять время окончания.");
    }

    @Test
    void testGetDate() {
        assertEquals(date, reservation.getDate(), "Дата должна соответствовать заданному значению.");
    }

    @Test
    void testSetDate() {
        LocalDate newDate = LocalDate.of(2024, 6, 24);
        reservation.setDate(newDate);
        assertEquals(newDate, reservation.getDate(), "Метод setDate должен корректно обновлять дату.");
    }

    @Test
    void testToString() {
        String expectedString = "ID: " + reservation.getId() +
                                " PlaceId: " + place.getId() +
                                " Тип места: " + reservation.getPlaceType().name() +
                                " Логин клиента: " + clientLogin +
                                " День:" + date +
                                " Время начала: " + startTime +
                                " Вермя конца: " + endTime;
        assertEquals(expectedString, reservation.toString(), "Метод toString должен корректно отображать информацию о бронировании.");
    }
}
