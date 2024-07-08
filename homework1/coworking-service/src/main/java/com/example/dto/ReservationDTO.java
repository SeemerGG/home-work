package com.example.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public record ReservationDTO(Integer idPlace, LocalTime startTime, LocalTime endTime, LocalDate date) {

}
