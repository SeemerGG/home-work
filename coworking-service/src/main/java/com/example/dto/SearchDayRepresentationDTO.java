package com.example.dto;

import java.time.LocalDate;

/**
 * Класс представления информации о свободных интервалах записи для транспортировки.
 */
public record SearchDayRepresentationDTO(String intervalsList, LocalDate date) {
}