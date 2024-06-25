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

import com.example.model.Place;
import com.example.model.PlaceType;
import com.example.model.Reservation;

public class ReservationDAOTest {

    private ReservationDAO reservationDAO;
    private Place place;
    private Reservation reservation;

    @BeforeEach
    void setUp() {
        reservationDAO = new ReservationDAO();
        place = new Place("Conference Room", 10);
        reservation = new Reservation(place, "userLogin", LocalDate.now(), LocalTime.of(9, 0), LocalTime.of(17, 0));
        Map<Integer, Reservation> list = new HashMap<>();
        list.put(reservation.getId(), reservation);
        reservationDAO.setList(list);
    }

    @Test
    @DisplayName("Добавление нового бронирования и получение его по ID")
    void testAddAndGetReservation() {
        reservationDAO.addReservation(place, "newUser", LocalDate.now(), LocalTime.of(10, 0), LocalTime.of(15, 0));
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
        int placeId = place.getId();
        reservationDAO.addReservation(place, "anotherUser", LocalDate.now(), LocalTime.of(11, 0), LocalTime.of(14, 0));
        reservationDAO.deleteElementForPlaceId(placeId);
        List<Reservation> reservations = reservationDAO.getReservations().values().stream()
            .filter(res -> res.getPlace().getId() == placeId)
            .collect(Collectors.toList());
        assertTrue(reservations.isEmpty(), "Бронирования для места должны быть удалены");
    }

    @Test
    @DisplayName("Получение бронирований на конкретный день")
    void testGetReservationsForDate() {
        LocalDate date = LocalDate.now();
        reservationDAO.addReservation(place, "userLogin", date, LocalTime.of(10, 0), LocalTime.of(15, 0));
        List<Reservation> reservationsForDate = reservationDAO.getReservationsForDate(date);
        assertFalse(reservationsForDate.isEmpty(), "Должны быть бронирования на указанную дату");
    }

    @Test
    @DisplayName("Получение бронирований на конкретное место")
    void testGetReservationsForPlace() {
        int idPlace = place.getId();
        List<Reservation> reservationsForPlace = reservationDAO.getReservationsForPlace(idPlace);
        assertFalse(reservationsForPlace.isEmpty(), "Должны быть бронирования на указанное место");
    }

    @Test
    @DisplayName("Получение бронирований одного пользователя")
    void testGetReservationsForLogin() {
        String login = "userLogin";
        List<Reservation> reservationsForLogin = reservationDAO.getReservationsForLogin(login);
        assertFalse(reservationsForLogin.isEmpty(), "Должны быть бронирования для указанного пользователя");
    }

    @Test
    @DisplayName("Получение бронирований по типу места")
    void testGetReservationsForType() {
        PlaceType type = PlaceType.CONFERENCEROOM;
        List<Reservation> reservationsForType = reservationDAO.getReservationsForType(type);
        assertFalse(reservationsForType.isEmpty(), "Должны быть бронирования для указанного типа места");
    }
}
