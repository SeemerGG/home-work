package com.example.service.imp;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.annotation.Loggable;
import com.example.domain.dto.SearchDayRepresentationDTO;
import com.example.domain.model.Place;
import com.example.domain.model.Reservation;
import com.example.out.dao.PlaceDAO;
import com.example.out.dao.ReservationDAO;
import com.example.service.SearchService;

@Service
@Loggable
/**
 * Класс реализующий логику поиска.
 */
public class SearchServiceImp implements SearchService {

    private final PlaceDAO placeDAO;
    private final ReservationDAO reservationDAO;

    //Это наверное  должно быть в классе с константами приложения
    private LocalTime openTime = LocalTime.of(8, 0);
    private LocalTime closeTime = LocalTime.of(22, 0);

    @Autowired
    public SearchServiceImp(PlaceDAO placeDAO, ReservationDAO reservationDAO) {

        this.placeDAO = placeDAO;
        this.reservationDAO = reservationDAO;
    }

    @Override
    public Collection<Place> search() throws Exception {

        return placeDAO.getPlaces().values();
    }

    @Override
    public SearchDayRepresentationDTO searchDay(LocalDate date) throws Exception {

        StringBuilder listPlacesWithTime = new StringBuilder("ID) *Остальная информация*\n");
        Map<Integer, Place> places = placeDAO.getPlaces();

        Stream<Reservation> stream = reservationDAO.getReservationsForDate(date).stream();
        Map<Integer, List<Reservation>> reservationsByPlace = stream.collect(Collectors.groupingBy(res -> res.getPlaceId()));
        reservationsByPlace.forEach((placeId, resList) -> {
            List<LocalTime> allIntervals = generatingAllTimeInterval();
            
            for (Reservation res : resList) {
                allIntervals.removeIf(time -> !time.isBefore(res.getStartTime()) && !time.isAfter(res.getEndTime()));
            }
            if(allIntervals.size() != 0) {

                try {
                    listPlacesWithTime.append(placeDAO.getPlace(resList.get(0).getPlaceId()).toString() + " : \n");
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                for (int i = 0; i < allIntervals.size() - 1; i++) {
                    listPlacesWithTime.append(allIntervals.get(i));
                    listPlacesWithTime.append(" - ");
                    listPlacesWithTime.append(allIntervals.get(i + 1));
                    listPlacesWithTime.append(", ");
                }
                listPlacesWithTime.append("\n");
            }

        });

        for(Map.Entry<Integer, Place> entry : places.entrySet()) {
            if(reservationsByPlace.keySet().contains(entry.getKey())) {
                places.remove(entry.getKey());
            }
        }

        for(Place place : places.values()) {
            listPlacesWithTime.append(place.toString() + " : \n");
            listPlacesWithTime.append("8 - 22\n");
        }
        return new SearchDayRepresentationDTO(listPlacesWithTime.toString(), date);
    }

    /**
     * Вспомогательный метод для генерации списка всех интервалов времени между временем открытия и закрытия офиса (8:00-22:00).
     * Создает список временных интервалов.
     * @return Список интервалов времени.
     */
    private List<LocalTime> generatingAllTimeInterval() {
        
        return IntStream.range(openTime.toSecondOfDay(), closeTime.toSecondOfDay())
                    .filter(i -> i % 3600 == 0)
                    .mapToObj(i -> LocalTime.ofSecondOfDay(i))
                    .collect(Collectors.toList());
    }

}

