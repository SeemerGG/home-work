package com.example.out.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Connection;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.lifecycle.Startables;

import com.example.infrastructure.ConfigurationProperties;
import com.example.infrastructure.MigrationConfig;
import com.example.model.Reservation;

public class ReservationDAOTest {

    private static ReservationDAO reservationDAO;
    private int placeId = 1;

    @Container
    private static PostgreSQLContainer<?> container;


    static {
        try {
            String containerVer = ConfigurationProperties.properties.getProperty("container.version");
            container = new PostgreSQLContainer<>(containerVer);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static Connection connection;

    @BeforeAll 
    static void runConfiguration() throws Exception {

        Startables.deepStart(Stream.of(container)).join();
        connection = container.createConnection("");
        MigrationConfig.performingMigration(connection);
        reservationDAO = new ReservationDAO(connection);
    }

    @Test
    @DisplayName("Добавление нового бронирования и получение его по ID")
    void testAddAndGetReservation() throws Exception {

        reservationDAO.addReservation(placeId, "newUser", LocalDate.now(), LocalTime.of(10, 0), LocalTime.of(15, 0));
        Reservation newReservation = reservationDAO.getReservation(1);
        assertNotNull(newReservation, "Бронирование должно быть добавлено и получено");
    }

    @Test
    @DisplayName("Удаление бронирования по ID")
    void testDeleteReservation() throws Exception {

        reservationDAO.delete(1);
        assertNull(reservationDAO.getReservation(1), "Бронирование должно быть удалено"); 
    }

    @Test
    @DisplayName("Обновление времени бронирования")
    void testUpdateTime() throws Exception {

        reservationDAO.addReservation(placeId, "newUser", LocalDate.now(), LocalTime.of(10, 0), LocalTime.of(15, 0));
        LocalTime newStartTime = LocalTime.of(10, 0);
        LocalTime newEndTime = LocalTime.of(16, 0);
        reservationDAO.updateTime(1, newStartTime, newEndTime);
        Reservation updatedReservation = reservationDAO.getReservation(1);
        assertEquals(newStartTime, updatedReservation.getStartTime(), "Время начала должно быть обновлено");
        assertEquals(newEndTime, updatedReservation.getEndTime(), "Время окончания должно быть обновлено");
    }

    @Test
    @DisplayName("Получение всех бронирований")
    void testGetReservations() throws Exception {

        assertFalse(reservationDAO.getReservations().isEmpty(), "Список бронирований не должен быть пустым");
    }

    @Test
    @DisplayName("Удаление бронирований для определенного места")
    void testDeleteElementForPlaceId() throws Exception {

        reservationDAO.addReservation(placeId, "anotherUser", LocalDate.now(), LocalTime.of(11, 0), LocalTime.of(14, 0));
        reservationDAO.deleteElementForPlaceId(placeId);
        List<Reservation> reservations = reservationDAO.getReservations().values().stream()
            .filter(res -> res.getPlaceId() == placeId)
            .collect(Collectors.toList());
        assertTrue(reservations.isEmpty(), "Бронирования для места должны быть удалены");
    }

    @AfterAll
    static void down() {
        
        MigrationConfig.closeMigration();
        container.stop();
    } 
}
