package com.example.service;

import java.time.LocalDate;
import java.util.Collection;

import com.example.domain.dto.SearchDayRepresentationDTO;
import com.example.domain.model.Place;

/**
 * Класс реализующий логику поиска.
 */
public interface SearchService {
    /**
     * Метод поиска всех мест.
     * Получает список всех рабочих мест и конференц-залов без учета их занятости и выводит его в представление.
     */
    public Collection<Place> search() throws Exception;

    /**
     * Метод поиска мест на определенную дату.
     * Фильтрует бронирования по дате и выводит список мест с доступными временными интервалами.
     * @param date Дата, на которую осуществляется поиск.
     */
    public SearchDayRepresentationDTO searchDay(LocalDate date) throws Exception;
}
