package com.example.domain.dto;

/**
 * Класс представления места для транспортировки.
 */
public record PlaceDTO(int id, String loginOwner, String placeType, int seats) {
}