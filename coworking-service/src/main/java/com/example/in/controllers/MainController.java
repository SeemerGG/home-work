package com.example.in.controllers;

import com.example.in.views.MainView;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import com.example.model.ConferenceRoom;
import com.example.model.Place;
import com.example.model.PlaceType;
import com.example.model.Reservation;
import com.example.out.dao.ConferenceRoomDAO;
import com.example.out.dao.ReservationDAO;
import com.example.out.dao.WorkPlaceDAO;

public class MainController {

    private String currUserLogin;
    private MainView mainView;
    private WorkPlaceDAO workPlaceDAO;
    private ReservationDAO reservationDAO;
    private ConferenceRoomDAO conferenceRoomDAO;

    //Это наверное  должно быть в классе с константами приложения
    private LocalTime openTime = LocalTime.of(8, 0);
    private LocalTime closeTime = LocalTime.of(22, 0);


    public MainController(String currUserLogin,
    WorkPlaceDAO workPlaceDAO, ConferenceRoomDAO conferenceRoomDAO, 
    ReservationDAO reservationDAO) {
        this.mainView = new MainView(this);
        this.workPlaceDAO = workPlaceDAO;
        this.conferenceRoomDAO = conferenceRoomDAO;
        this.reservationDAO = reservationDAO;
    }

    public void authorized(String currUserLogin) {
        this.currUserLogin = currUserLogin;
        mainView.run(this.currUserLogin);
    }

    public void search() {
        List<Place> places = listAllPlaces();

        mainView.printList(places);
    }

    public void searchDay(LocalDate date) {
        StringBuilder listPlacesWithTime = new StringBuilder("ID) *Остальная информация*\n");

        //List<Place> places = listAllPlaces();

        List<Reservation> reservations = reservationDAO.getReservations();

        Stream<Reservation> stream = reservations.stream().filter(res -> res.getDate().isEqual(date));
        Map<Integer, List<Reservation>> reservationsByPlace = stream.collect(Collectors.groupingBy(res -> res.getPlace().getId()));
        reservationsByPlace.forEach((placeId, resList) -> {
            // Создание полного списка интервалов времени открытия до закрытия
            List<LocalTime> allIntervals = generatingAllTimeInterval();

            // Удаление занятых интервалов
            for (Reservation res : resList) {
                allIntervals.removeIf(time -> !time.isBefore(res.getStartTime()) && !time.isAfter(res.getEndTime()));
            }

            // Вывод свободных интервалов
            if(allIntervals.size() != 0) {
                listPlacesWithTime.append(String.format("%d)", placeId));
                listPlacesWithTime.append(resList.get(0).getPlace().toString() + " : ");
                for (int i = 0; i < allIntervals.size() - 1; i++) {
                    listPlacesWithTime.append(allIntervals.get(i));
                    listPlacesWithTime.append(" - ");
                    listPlacesWithTime.append(allIntervals.get(i + 1));
                    listPlacesWithTime.append(", ");
                }
            }
        });
        mainView.print(listPlacesWithTime.toString());
        mainView.reservation(reservationsByPlace);
    }

    //Проверяет дату и резервирует если может
    public void reservating(Map<Integer, List<Reservation>> reservationByPlace, Integer idPlace, LocalTime starTime, LocalTime endTime) {
        if(reservationByPlace.containsKey(idPlace)) {
            for(Reservation reserv : reservationByPlace.get(idPlace)) {
                if(!reserv.getStartTime().isAfter(starTime) && 
                !reserv.getEndTime().isAfter(endTime)) {
                    mainView.sayError("Указанный период уже занят (полностью или частично)!");
                }
                else {
                    reservationDAO.addReservation(reservationByPlace.get(idPlace).get(0).getPlace(), 
                    currUserLogin, reservationByPlace.get(idPlace).get(0).getDate(), starTime, endTime);
                    mainView.print("Место успешно забронированно!");
                }
            }
        }
        else {
            mainView.sayError("Неверно указан ID места!");
        }
    }

    private List<LocalTime> generatingAllTimeInterval() {
        return IntStream.range(openTime.toSecondOfDay(), closeTime.toSecondOfDay())
                    .filter(i -> i % 1800 == 0)
                    .mapToObj(i -> LocalTime.ofSecondOfDay(i))
                    .collect(Collectors.toList());
    }

    private List<Place> listAllPlaces() {
        List<Place> places = new ArrayList<>();
        places.addAll(workPlaceDAO.getPlaces());
        places.addAll(conferenceRoomDAO.getPlaces());
        return places;
    }

    public void myPublication() {
        List<Place> places = listAllPlaces().stream().filter(p -> p.getLoginOwner().equals(currUserLogin)).toList();
        mainView.myPlaceAction(places);
    }

    public void deleteMyPlace(int id) {
        try {
            reservationDAO.deleteElementForPlaceId(id);
            Place place = listAllPlaces().stream().filter(p -> p.getId() == id).findAny().orElse(null);
            if(place.getClass() == ConferenceRoom.class) {
                conferenceRoomDAO.deleteElement(id);
            }
            else {
                workPlaceDAO.deletePlace(id);
            }
            mainView.print("Удаление завершено!");
        } catch (Exception e) {
            mainView.sayError(e.getMessage());
        }
    }

    public void createMyPlace() {
        workPlaceDAO.addWorkPlace(currUserLogin);
        mainView.print("Добавление завершено!");
    }

    public void createMyPlace(int seats) {
        conferenceRoomDAO.addConferenseRoom(currUserLogin, seats);
        mainView.print("Добавление завершено!");
    }

    public void updateTime(int id, LocalTime startTime, LocalTime endTime) {
        Reservation reservation = reservationDAO.getReservation(id);
        try {
            if(id<0||reservation == null){
                throw new Exception("Неверный ID!");
            }
            else {
                List<Reservation> listReserv = reservationDAO.getReservations().stream().filter(res -> 
                (res.getPlace().getId() == reservation.getPlace().getId())&&(res.getDate().isEqual(reservation.getDate()))).toList();
                listReserv.remove(id);
                for(Reservation reserv : listReserv) {
                    if(!reserv.getStartTime().isAfter(startTime) && 
                    !reserv.getEndTime().isAfter(endTime)) {
                        mainView.sayError("Указанный период уже занят (полностью или частично)!");
                    }
                    else {
                        reservationDAO.updateTime(id, startTime, endTime);
                        mainView.print("Запись успешно изменена!");
                    }
                }
            }
        } catch (Exception e) {
            mainView.sayError(e.getMessage());
        }
    }

    public void reservOut() {
        List<Reservation> list = reservationDAO.getReservations().stream().filter(res -> res.getClientLogin() == currUserLogin).toList();
        StringBuilder sb = new StringBuilder();
        for(Reservation res : list) {
            sb.append(res.toString());
            sb.append("\n");
        }
        mainView.print(sb.toString());
    }

    public void deleteReservaton(int id) {
        try {
            reservationDAO.delete(id);
        } catch (Exception e) {
            mainView.sayError(e.getMessage());
        }
    }

    public void filterForType(String param) {
        List<Reservation> list = reservationDAO.getReservations().stream()
                        .filter(res -> res.getClientLogin() == currUserLogin)
                        .toList();
        switch (param) {
            case "conference":
                list = list.stream().filter(res -> res.getPlaceType() == PlaceType.CONFERENCEROOM).toList();
                reservOut(list);
                break;
            case "work":
                list = list.stream().filter(res -> res.getPlaceType() == PlaceType.WORKPLACE).toList();
                reservOut(list);
                break;
        }
        reservOut(list);
    }

    public void filterForDate(LocalDate date) {
        List<Reservation> list = reservationDAO.getReservations().stream()
                        .filter(res -> res.getClientLogin() == currUserLogin)
                        .toList();
        Collections.sort(list, (obj1, obj2) -> obj1.getDate().compareTo(obj2.getDate()));
        reservOut(list);
    }

    public void filterForOwner() {
        List<Reservation> list = reservationDAO.getReservations().stream()
            .filter(res -> res.getClientLogin() == currUserLogin)
            .toList();
        Collections.sort(list, (obj1, obj2) -> obj1.getPlace().getLoginOwner().compareTo(obj2.getPlace().getLoginOwner()));
        reservOut(list);
    }

    public void reservOut(List<Reservation> list) {
        StringBuilder sb = new StringBuilder();
        for(Reservation res : list) {
            sb.append(res.toString());
            sb.append("\n");
        }
        mainView.print(sb.toString());
    }
}
