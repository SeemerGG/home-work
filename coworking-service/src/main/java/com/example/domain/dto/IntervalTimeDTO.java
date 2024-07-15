package com.example.domain.dto;

import java.time.LocalTime;

/**
 * Класс представления временного интервала для транспортировки.
 */
public record IntervalTimeDTO(LocalTime startTime, LocalTime endTime) {
}