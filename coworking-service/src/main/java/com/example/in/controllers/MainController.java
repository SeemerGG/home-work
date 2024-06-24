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

/**
 * Контроллер основного интерфейса приложения, управляющий взаимодействием пользователя с системой бронирования.
 * Этот класс содержит логику для поиска и бронирования рабочих мест и конференц-залов, а также добавления и редактирования этих мест.
 */
public class MainController {

    private String currUserLogin;
    private MainView mainView;
    private WorkPlaceDAO workPlaceDAO;
    private ReservationDAO reservationDAO;
    private ConferenceRoomDAO conferenceRoomDAO;

    //Это наверное  должно быть в классе с константами приложения
    private LocalTime openTime = LocalTime.of(8, 0);
    private LocalTime closeTime = LocalTime.of(22, 0);


    /**
     * Конструктор класса.
     * @param currUserLogin Логин аутентифицированного пользователя.
     * @param workPlaceDAO DAO для работы с рабочими местами.
     * @param conferenceRoomDAO DAO для работы с конференц-залами.
     * @param reservationDAO DAO для работы с бронированиями.
     */
    public MainController(String currUserLogin,
    WorkPlaceDAO workPlaceDAO, ConferenceRoomDAO conferenceRoomDAO, 
    ReservationDAO reservationDAO) {
        this.mainView = new MainView(this);
        this.workPlaceDAO = workPlaceDAO;
        this.conferenceRoomDAO = conferenceRoomDAO;
        this.reservationDAO = reservationDAO;
    }

    /**
     * Метод вызываемый после авторизации пользователя для запуска основного интерфейса.
     * @param currUserLogin Логин авторизованного пользователя.
     */
    public void authorized(String currUserLogin) {
        this.currUserLogin = currUserLogin;
        mainView.run(this.currUserLogin);
    }

    /**
     * Метод поиска всех мест.
     * Получает список всех рабочих мест и конференц-залов без учета их занятости и выводит его в представление.
     */
    public void search() {
        List<Place> places = listAllPlaces();

        mainView.printList(places);
    }

    /**
     * Метод поиска мест на определенную дату.
     * Фильтрует бронирования по дате и выводит список мест с доступными временными интервалами.
     * @param date Дата, на которую осуществляется поиск.
     */
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

     /**
     * Метод бронирования места.
     * Проверяет доступность места и осуществляет бронирование, если это возможно.
     * Словарь содержит элементы свободные на дату выбранную в методе searchDay.
     * @param reservationByPlace Словарь где ключем выступает id места, а значением лист записей на это место.
     * @param idPlace Идентификатор места для бронирования.
     * @param starTime Время начала бронирования.
     * @param endTime Время окончания бронирования.
     */
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

    /**
     * Вспомогательный метод для генерации списка всех интервалов времени между временем открытия и закрытия офиса (8:00-22:00).
     * Создает список временных интервалов.
     * @return Список интервалов времени.
     */
    private List<LocalTime> generatingAllTimeInterval() {
        return IntStream.range(openTime.toSecondOfDay(), closeTime.toSecondOfDay())
                    .filter(i -> i % 1800 == 0)
                    .mapToObj(i -> LocalTime.ofSecondOfDay(i))
                    .collect(Collectors.toList());
    }
    /**
     * Метод возвращающий все места зарегистрированные в системе.
     * @return Список всех мест зарегистрированных в системе.
     */
    private List<Place> listAllPlaces() {
        List<Place> places = new ArrayList<>();
        places.addAll(workPlaceDAO.getPlaces());
        places.addAll(conferenceRoomDAO.getPlaces());
        return places;
    }

    /**
     * Метод ищет все места зарегистрированные на логин текущего пользователя и передает их представлению для вывода.
     */
    public void myPublication() {
        List<Place> places = listAllPlaces().stream().filter(p -> p.getLoginOwner().equals(currUserLogin)).toList();
        mainView.myPlaceAction(places);
    }

    /**
     * Метод удаляет место с указанным id, а также все записи с указаным местом.
     * @param id Идентификационный номер места, которое надо удалить.
     */
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

    /**
     * Создает новое рабочее место для текущего пользователя.
     * Добавляет рабочее место в базу данных и обновляет представление.
     */
    public void createMyPlace() {
        workPlaceDAO.addWorkPlace(currUserLogin);
        mainView.print("Добавление завершено!");
    }

    /**
     * Создает новый конференц-зал для текущего пользователя.
     * Добавляет конференц-зал с указанным количеством мест в систему и обновляет представление.
     * @param seats Количество мест в новом конференц-зале.
     */
    public void createMyPlace(int seats) {
        conferenceRoomDAO.addConferenseRoom(currUserLogin, seats);
        mainView.print("Добавление завершено!");
    }

    /**
     * Обновляет время бронирования для указанного идентификатора.
     * Проверяет наличие конфликтов с существующими бронированиями и обновляет время, если это возможно.
     * @param id Идентификатор бронирования для обновления.
     * @param startTime Новое время начала бронирования.
     * @param endTime Новое время окончания бронирования.
     */
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


    /**
     * Генерирует список всех бронирований текущего пользователя ввиде строки.
     * Передает полученную строку в представление для отображения.
     */
    public void reservOut() {
        List<Reservation> list = reservationDAO.getReservations().stream().filter(res -> res.getClientLogin() == currUserLogin).toList();
        StringBuilder sb = new StringBuilder();
        for(Reservation res : list) {
            sb.append(res.toString());
            sb.append("\n");
        }
        mainView.print(sb.toString());
    }

    /**
     * Удаляет бронирование по его идентификатору.
     * Удаляет бронирование из системы и обновляет представление.
     * @param id Идентификатор бронирования для удаления.
     */
    public void deleteReservaton(int id) {
        try {
            reservationDAO.delete(id);
        } catch (Exception e) {
            mainView.sayError(e.getMessage());
        }
    }

    /**
     * Фильтрует бронирования по типу места.
     * Передает представлению список бронирований, соответствующих указанному типу места, для отображения.
     * @param param Тип места для фильтрации ('conference' или 'work').
     */
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

    /**
     * Фильтрует бронирования по дате.
     * Сортирует и передает представлению список бронирований пользователя отсортированных по дате.
     */
    public void filterForDate() {
        List<Reservation> list = reservationDAO.getReservations().stream()
                        .filter(res -> res.getClientLogin() == currUserLogin)
                        .toList();
        Collections.sort(list, (obj1, obj2) -> obj1.getDate().compareTo(obj2.getDate()));
        reservOut(list);
    }

    /**
     * Фильтрует бронирования по владельцу места.
     * Сортирует и передает представлению список бронирований пользователя по владельцу места.
     */
    public void filterForOwner() {
        List<Reservation> list = reservationDAO.getReservations().stream()
            .filter(res -> res.getClientLogin() == currUserLogin)
            .toList();
        Collections.sort(list, (obj1, obj2) -> obj1.getPlace().getLoginOwner().compareTo(obj2.getPlace().getLoginOwner()));
        reservOut(list);
    }

    /**
     * Выводит список бронирований.
     * Принимает список бронирований и передает его представлению для вывода пользователю.
     * @param list Список бронирований для вывода.
     */
    public void reservOut(List<Reservation> list) {
        StringBuilder sb = new StringBuilder();
        for(Reservation res : list) {
            sb.append(res.toString());
            sb.append("\n");
        }
        mainView.print(sb.toString());
    }
}
