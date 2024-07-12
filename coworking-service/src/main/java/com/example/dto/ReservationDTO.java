package com.example.dto;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Класс представления бронирований для транспортировки.
 */
public record ReservationDTO(Integer idPlace, LocalTime startTime, LocalTime endTime, LocalDate date) {
}