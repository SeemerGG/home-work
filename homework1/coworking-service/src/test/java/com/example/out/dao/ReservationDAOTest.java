package com.example.out.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.example.model.Reservation;

public class ReservationDAOTest {

    private ReservationDAO reservationDAO;
    private int placeId = 1;
    private Reservation reservation;

    @BeforeEach
    void setUp() {
        reservationDAO = new ReservationDAO();
        reservation = new Reservation(placeId, "userLogin", LocalDate.now(), LocalTime.of(9, 0), LocalTime.of(17, 0));
        Map<Integer, Reservation> list = new HashMap<>();
        list.put(reservation.getId(), reservation);
        reservationDAO.setList(list);
    }

    @Test
    @DisplayName("Добавление нового бронирования и получение его по ID")
    void testAddAndGetReservation() {
        reservationDAO.addReservation(placeId, "newUser", LocalDate.now(), LocalTime.of(10, 0), LocalTime.of(15, 0));
        Reservation newReservation = reservationDAO.getReservation(reservation.getId() + 1);
        assertNotNull(newReservation, "Бронирование должно быть добавлено и получено");
    }

    @Test
    @DisplayName("Удаление бронирования по ID")
    void testDeleteReservation() {
        reservationDAO.delete(reservation.getId());
        assertNull(reservationDAO.getReservation(reservation.getId()), "Бронирование должно быть удалено");
    }

    @Test
    @DisplayName("Обновление времени бронирования")
    void testUpdateTime() {
        LocalTime newStartTime = LocalTime.of(10, 0);
        LocalTime newEndTime = LocalTime.of(16, 0);
        reservationDAO.updateTime(reservation.getId(), newStartTime, newEndTime);
        Reservation updatedReservation = reservationDAO.getReservation(reservation.getId());
        assertEquals(newStartTime, updatedReservation.getStartTime(), "Время начала должно быть обновлено");
        assertEquals(newEndTime, updatedReservation.getEndTime(), "Время окончания должно быть обновлено");
    }

    @Test
    @DisplayName("Получение всех бронирований")
    void testGetReservations() {
        assertFalse(reservationDAO.getReservations().isEmpty(), "Список бронирований не должен быть пустым");
    }

    @Test
    @DisplayName("Удаление бронирований для определенного места")
    void testDeleteElementForPlaceId() {
        reservationDAO.addReservation(placeId, "anotherUser", LocalDate.now(), LocalTime.of(11, 0), LocalTime.of(14, 0));
        reservationDAO.deleteElementForPlaceId(placeId);
        List<Reservation> reservations = reservationDAO.getReservations().values().stream()
            .filter(res -> res.getPlaceId() == placeId)
            .collect(Collectors.toList());
        assertTrue(reservations.isEmpty(), "Бронирования для места должны быть удалены");
    }

}
