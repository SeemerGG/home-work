package com.example.domain.dto;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Класс представления бронирований для транспортировки.
 */
public record ReservationDTO(int id, String  clientLogin, Integer placeId, LocalTime startTime, LocalTime endTime, LocalDate date) {
}