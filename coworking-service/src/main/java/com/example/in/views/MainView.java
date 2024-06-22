package com.example.in.views;

import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import com.example.in.controllers.MainController;
import com.example.model.Place;
import com.example.model.Reservation;


public class MainView {

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private DateTimeFormatter formatterTime = DateTimeFormatter.ofPattern("HH:mm");
    private MainController mainController;
    private Scanner scanner = new Scanner(System.in);

    public MainView(MainController mainController) {
        this.mainController = mainController;
    }

    public void run(String login) {
        System.out.println("Здравствуйте " + login);
        switchAction();
    }

    private void switchAction() {
        String str = new String();
        while (true) {
            System.out.println("Перечень команд: \n1)Просмотр всех доступных мест(search)" +
        "\n2)Просмотр забронированных мест(reserv) \n3)Публикация новых мест(publ)" + 
        "\n4)Просмотр мест на конкретную дату(searchDay) \n5)Выйти(exit):");
            str = scanner.nextLine();
            if(str.equals("exit")){
                break;
            }
            switch (str) {
                case "search":
                    System.out.println("Список всех зарегистрированных мест: ");
                    mainController.search();
                    break;
                case "searchDay":
                    System.out.println("Введите дату в формате dd.MM.yyyy:");
                    try {
                        LocalDate date = LocalDate.parse(scanner.nextLine(), formatter);
                        mainController.searchDay(date);
                    } catch (Exception e) {
                        sayError(e.getMessage());
                    }
                    break;
                case "publ":
                    System.out.println("Места опубликованные вами: ");
                    mainController.myPublication();
                    break;
                case "reserv":
                    reservAction();
                    break;
                default:
                    System.out.println("Вы неверно ввели команду, попробуйте снова: ");
                    break;
            }
        }
        
    }

    public void reservAction() {
        String str = new String();
        mainController.reservOut();
        while (true) {
            System.out.println("Редактировать бронь(update)/Удалить бронь(delete)/Фильтрация списка(filter)/Выход в меню(exit):");
            str = scanner.nextLine();
            if(str.equals("exit")) {
                break;
            }
            switch (str) {
                case "update":
                    System.out.println("Введите ID записи:");
                    try {
                        Integer id = scanner.nextInt();
                        System.out.println("Введите новый интервал записи в формате HH:mm HH:mm: ");
                        String[] strings = scanner.nextLine().split(" ");
                        LocalTime startTime = LocalTime.parse(strings[0], formatterTime);
                        LocalTime endTime = LocalTime.parse(strings[1], formatterTime);
                        if(endTime.isBefore(startTime)) {
                            throw new Exception("Неправильный временной порядок!");
                        }
                        mainController.updateTime(id, startTime, endTime);
                        mainController.reservOut();
                    }
                    catch(Exception e) {
                        sayError(e.getMessage());
                    }
                    break;
                case "delete":
                    System.out.println("Введите ID записи: ");
                    try {
                        Integer id = scanner.nextInt();
                        mainController.deleteReservaton(id);
                        mainController.reservOut();
                    } catch (Exception e) {
                        sayError(e.getMessage());
                    }
                    break;
                case "filter":
                    System.out.println("Введите параметр фильтрации(type/date/owner):");
                    String command = new String();
                    switch (command) {
                        case "type":
                            System.out.println("Введите тип места(conference/work):");
                            String type = scanner.nextLine();
                            if(type.equals("conference")) {
                                mainController.filterForType(type);
                            }
                            else if(type.equals("work")) {
                                mainController.filterForType(type);
                            }
                            else {
                                sayError("Такого типа места не существует!");
                            }
                            break;
                        case "date":
                            System.out.println("Введите дату в формате dd.MM.yyyy:");
                            try {
                                LocalDate date = LocalDate.parse(scanner.nextLine(), formatter);
                                mainController.filterForDate(date);
                            } catch (Exception e) {
                                sayError(e.getMessage());
                            }
                            break;
                        case "owner":
                            try {
                                mainController.filterForOwner();
                            } catch (Exception e) {
                                sayError(e.getMessage());
                            }
                            break;
                        default:
                            sayError("Не корректная команда!");
                            break;
                    }
                    break;
                default:
                    System.out.println("Вы неверно ввели команду, попробуйте снова:");
                    break;
            }
        }
    }
    

    public void myPlaceAction(List<Place> myPlaces) {
        String command = new String();
        printList(myPlaces);
        while(true) {
            System.out.println("Удаления места(delete)/Добавление места(create)/Выход в меню (exit):");
            command = scanner.nextLine();
            if(command.equals("exit")) {
                break;
            }
            switch (command) {
                case "delete":
                    try {
                        System.out.println("Введите ID места: ");
                        Integer id = scanner.nextInt();
                        mainController.deleteMyPlace(id);
                    } catch (Exception e) {
                        sayError(e.getMessage());
                    }
                    break;
                case "create":
                    try {
                        System.out.println("Введите тип места(conference/work):");
                        String type = scanner.nextLine();
                        System.out.println(type);
                        if(type.equals("conference")) {
                            System.out.println("Введите количество мест в зале: ");
                            Integer i = scanner.nextInt();
                            if(i < 1) { 
                                throw new Exception("Количество мест в зале не может быть меньше 1!");
                            }
                            mainController.createMyPlace(i);
                        }
                        else if(type.equals("work")) {
                            mainController.createMyPlace();
                        }     
                        else {
                            System.out.println("Неверная команда. Попробуйте снова: ");
                        }
                    } 
                    catch (Exception e) {
                        sayError(e.getMessage());
                    }
                    break;
                default:
                    System.out.println("Неверная команда. Попробуйте снова: ");
                    break;
            }
        }
    }
    
    public void reservation(Map<Integer, List<Reservation>> reservationByPlace) {
        String str = new String();
        while(true) {
            System.out.println("Бронирование(reserv)/Выйти в меню(exit):");
            str = scanner.nextLine();
            if(str.equals("exit"))
            {
                break;
            }
            switch (str) {
                case "reserv":
                    System.out.println("Введите ID лота: ");
                    try {
                        Integer i = scanner.nextInt();
                        System.out.println("Введите интервал желаемой записи в формате HH:mm HH:mm: ");
                        String[] strings = scanner.nextLine().split(" ");
                        LocalTime startTime = LocalTime.parse(strings[0], formatterTime);
                        LocalTime endTime = LocalTime.parse(strings[1], formatterTime);
                        if(endTime.isBefore(startTime)) {
                            throw new Exception("Неправильный временной порядок!");
                        }
                        mainController.reservating(reservationByPlace, i, startTime, endTime);
                    } 
                    catch (Exception e) {
                        sayError(e.getMessage());
                    }
                    break;
                default:
                    System.out.println("Вы неверно ввели команду, попробуйте снова: ");
                    break;
            }
        }
    }

    public void printList(List<Place> list) {
        StringBuilder sb = new StringBuilder();

        for(int i = 0; i < list.size(); i++) {
            sb.append(i+")");
            sb.append(list.get(i).toString());
            sb.append("\n");
        }
        System.out.println(sb.toString());
    }

    public void print(String str) {
        System.out.println(str);
    }

    public void sayError(String errorMessage) {
        System.out.println("Произошла ошибка: ");
        System.out.println(errorMessage);
        System.out.println("Попробуйте снова.");
    }
}
