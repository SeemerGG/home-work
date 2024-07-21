package com.example.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.LocalTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.example.domain.model.Reservation;

public class ReservationTest {

    private Reservation reservation;
    private int placeId = 1;
    private final String clientLogin = "clientLogin";
    private final LocalDate date = LocalDate.of(2024, 6, 23);
    private final LocalTime startTime = LocalTime.of(14, 0);
    private final LocalTime endTime = LocalTime.of(16, 0);

    @BeforeEach
    void setUp() {

        reservation = new Reservation(1, placeId, clientLogin, date, startTime, endTime);
    }

    @Test
    @DisplayName("Проверка логина клиента.")
    void testGetClientLogin() {

        assertEquals(clientLogin, reservation.getClientLogin(), "Логин клиента должен соответствовать заданному значению.");
    }

    @Test
    @DisplayName("Проверка положительности ID бронирования.")
    void testGetId() {

        assertTrue(reservation.getId() > 0, "ID бронирования должен быть положительным числом.");
    }

    @Test
    @DisplayName("Проверка соответствия времени начала.")
    void testGetStartTime() {

        assertEquals(startTime, reservation.getStartTime(), "Время начала должно соответствовать заданному значению.");
    }

    @Test
    @DisplayName("Проверка обновления времени начала.")
    void testSetStartTime() {

        LocalTime newStartTime = LocalTime.of(15, 0);
        reservation.setStartTime(newStartTime);
        assertEquals(newStartTime, reservation.getStartTime(), "Метод setStartTime должен корректно обновлять время начала.");
    }

    @Test
    @DisplayName("Проверка соответствия времени окончания.")
    void testGetEndTime() {

        assertEquals(endTime, reservation.getEndTime(), "Время окончания должно соответствовать заданному значению.");
    }

    @Test
    @DisplayName("Проверка обновления времени окончания.")
    void testSetEndTime() {

        LocalTime newEndTime = LocalTime.of(17, 0);
        reservation.setEndTime(newEndTime);
        assertEquals(newEndTime, reservation.getEndTime(), "Метод setEndTime должен корректно обновлять время окончания.");
    }

    @Test
    @DisplayName("Проверка соответствия даты бронирования.")
    void testGetDate() {

        assertEquals(date, reservation.getDate(), "Дата должна соответствовать заданному значению.");
    }

    @Test
    @DisplayName("Проверка обновления даты бронирования.")
    void testSetDate() {

        LocalDate newDate = LocalDate.of(2024, 6, 24);
        reservation.setDate(newDate);
        assertEquals(newDate, reservation.getDate(), "Метод setDate должен корректно обновлять дату.");
    }

    @Test
    @DisplayName("Проверка корректности строкового представления.")
    void testToString() {
        
        String expectedString = "ID: " + reservation.getId() +
                                " PlaceId: " + placeId +
                                " Логин клиента: " + clientLogin +
                                " День:" + date +
                                " Время начала: " + startTime +
                                " Вермя конца: " + endTime;
        assertEquals(expectedString, reservation.toString(), "Метод toString должен корректно отображать информацию о бронировании.");
    }
}
