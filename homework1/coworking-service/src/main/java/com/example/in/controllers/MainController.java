package com.example.in.controllers;

import com.example.in.views.MainView;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import com.example.model.Place;
import com.example.model.PlaceType;
import com.example.model.Reservation;
import com.example.out.dao.PlaceDAO;
import com.example.out.dao.ReservationDAO;

/**
 * Контроллер основного интерфейса приложения, управляющий взаимодействием пользователя с системой бронирования.
 * Этот класс содержит логику для поиска и бронирования рабочих мест и конференц-залов, а также добавления и редактирования этих мест.
 */
public class MainController {

    private String currUserLogin;
    private MainView mainView;
    private ReservationDAO reservationDAO;
    private PlaceDAO placeDAO;

    //Это наверное  должно быть в классе с константами приложения
    private LocalTime openTime = LocalTime.of(8, 0);
    private LocalTime closeTime = LocalTime.of(22, 0);


    /**
     * Конструктор класса.
     * @param currUserLogin Логин аутентифицированного пользователя.
     * @param placeDAO DAO для работы с местами.
     * @param reservationDAO DAO для работы с бронированиями.
     */
    public MainController(String currUserLogin, PlaceDAO placeDAO, ReservationDAO reservationDAO) {
        this.mainView = new MainView(this);
        this.placeDAO = placeDAO;
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
    public void search() throws SQLException{
        mainView.printList(placeDAO.getPlaces().values());
    }

    /**
     * Метод поиска мест на определенную дату.
     * Фильтрует бронирования по дате и выводит список мест с доступными временными интервалами.
     * @param date Дата, на которую осуществляется поиск.
     */
    public void searchDay(LocalDate date) throws SQLException {
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
                } catch (Exception e) {
                    mainView.sayError(e.getMessage());
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

        mainView.print(listPlacesWithTime.toString());
        mainView.reservation(reservationsByPlace, date);
    }

     /**
     * Метод бронирования места.
     * Проверяет доступность места и осуществляет бронирование, если это возможно.
     * Словарь содержит элементы свободные на дату выбранную в методе searchDay.
     * @param reservationByPlace Словарь где ключем выступает id места, а значением лист записей на это место.
     * @param idPlace Идентификатор места для бронирования.
     * @param starTime Время начала бронирования.
     * @param endTime Время окончания бронирования.
     * @param date День начала бронирования.
     */
    public void reservating(Map<Integer, List<Reservation>> reservationByPlace, Integer idPlace, LocalTime starTime, LocalTime endTime, LocalDate date) throws SQLException{
        if(reservationByPlace.containsKey(idPlace)) {
            for(Reservation reserv : reservationByPlace.get(idPlace)) {
                if(!reserv.getStartTime().isAfter(starTime) && 
                !reserv.getEndTime().isAfter(endTime)) {
                    mainView.sayError("Указанный период уже занят (полностью или частично)!");
                }
                else {
                    reservationDAO.addReservation(idPlace, currUserLogin, 
                    reservationByPlace.get(idPlace).get(0).getDate(), starTime, endTime);
                    mainView.print("Место успешно забронированно!");
                }
            }
        }
        else if(placeDAO.exist(idPlace)){
            reservationDAO.addReservation(idPlace, currUserLogin, date, starTime, endTime); 
            mainView.print("Место успешно забронированно!");
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
     * Метод ищет все места зарегистрированные на логин текущего пользователя и передает их представлению для вывода.
     */
    public void myPublication() throws SQLException{
        mainView.myPlaceAction(placeDAO.getPlacesOneOwner(currUserLogin));
    }

    /**
     * Метод удаляет место с указанным id, а также все записи с указаным местом.
     * @param id Идентификационный номер места, которое надо удалить.
     */
    public void deleteMyPlace(int id) {
        try {
            reservationDAO.deleteElementForPlaceId(id);
            placeDAO.deletePlace(id);
            mainView.print("Удаление завершено!");
        } catch (Exception e) {
            mainView.sayError(e.getMessage());
        }
    }

    /**
     * Создает новый конференц-зал для текущего пользователя.
     * Добавляет конференц-зал с указанным количеством мест в систему и обновляет представление.
     * @param seats Количество мест в новом конференц-зале.
     */
    public void createMyPlace(int seats, PlaceType placeType) throws SQLException{
        placeDAO.add(currUserLogin, seats, placeType);
        mainView.print("Добавление завершено!");
    }

    /**
     * Обновляет время бронирования для указанного идентификатора.
     * Проверяет наличие конфликтов с существующими бронированиями и обновляет время, если это возможно.
     * @param id Идентификатор бронирования для обновления.
     * @param startTime Новое время начала бронирования.
     * @param endTime Новое время окончания бронирования.
     */
    public void updateTime(int id, LocalTime startTime, LocalTime endTime) throws SQLException{
        Reservation reservation = reservationDAO.getReservation(id);
        try {
            if(id<0||reservation == null){
                throw new Exception("Неверный ID!");
            }
            else {
                List<Reservation> listReserv = reservationDAO.getReservationsForDate(reservation.getDate()).stream().filter(res -> 
                res.getPlaceId() == reservation.getPlaceId()).toList();
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
    public void reservOut() throws SQLException{
        Collection<Reservation> list = reservationDAO.getReservationsForLogin(currUserLogin);
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
    public void filterForType() throws SQLException {
        reservOut(reservationDAO.getOrderedReservationByType(currUserLogin));
    }

    /**
     * Фильтрует бронирования по дате.
     * Сортирует и передает представлению список бронирований пользователя отсортированных по дате.
     */
    public void filterForDate() throws SQLException{
        reservOut(reservationDAO.getOrderedReservationByDay(currUserLogin));
    }

    /**
     * Фильтрует бронирования по владельцу места.
     * Сортирует и передает представлению список бронирований пользователя по владельцу места.
     */
    public void filterForOwner() throws SQLException{
        reservOut(reservationDAO.getOrderedReservationByOwner(currUserLogin));
    }

    /**
     * Выводит список бронирований.
     * Принимает список бронирований и передает его представлению для вывода пользователю.
     * @param list Список бронирований для вывода.
     */
    public void reservOut(Collection<Reservation> list) {
        StringBuilder sb = new StringBuilder();
        for(Reservation res : list) {
            sb.append(res.toString());
            sb.append("\n");
        }
        mainView.print(sb.toString());
    }
}
