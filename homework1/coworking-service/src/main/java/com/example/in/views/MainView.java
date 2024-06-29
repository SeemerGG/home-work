package com.example.in.views;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import com.example.in.controllers.MainController;
import com.example.model.Place;
import com.example.model.Reservation;

/**
 * Класс представления основного интерфейса пользователя.
 * Обеспечивает взаимодействие с пользователем через консоль и делегирует действия контроллеру.
 */
public class MainView {

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private DateTimeFormatter formatterTime = DateTimeFormatter.ofPattern("HH:mm");
    private MainController mainController;
    private Scanner scanner = new Scanner(System.in);

    /**
     * Конструктор класса представления.
     * @param mainController Контроллер основного интерфейса, с которым представление будет взаимодействовать.
     */
    public MainView(MainController mainController) {
        this.mainController = mainController;
    }

    /**
     * Запускает интерфейс пользователя.
     * @param login Логин пользователя, приветствующего его при входе в систему.
     */
    public void run(String login) {
        System.out.println("Здравствуйте " + login);
        switchAction();
    }

    /**
     * Отображает меню команд и обрабатывает ввод пользователя.
     * Позволяет пользователю выбрать действие и делегирует выполнение контроллеру.
     */
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
                    try {
                        mainController.search();
                    } catch (Exception e) {
                        sayError(e.getMessage());
                    }
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
                    try {
                        mainController.myPublication();
                    } catch (Exception e) {
                        sayError(e.getMessage());
                    }
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

    /**
     * Обрабатывает действия, связанные с бронированием.
     * Позволяет пользователю обновить или удалить бронирование, а также отфильтровать список бронирований.
     */
    public void reservAction() {
        String str = new String();
        try {
            mainController.reservOut();
        } catch (Exception e) {
            sayError(e.getMessage());
        }
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
                        Integer id = Integer.parseInt(scanner.nextLine());
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
                        Integer id = Integer.parseInt(scanner.nextLine());
                        mainController.deleteReservaton(id);
                        mainController.reservOut();
                    } catch (Exception e) {
                        sayError(e.getMessage());
                    }
                    break;
                case "filter":
                    System.out.println("Введите параметр фильтрации(type/date/owner):");
                    String command = scanner.nextLine();
                    switch (command) {
                        case "type":
                            System.out.println("Введите тип места(conference/work):");
                            String type = scanner.nextLine();
                            if(type.equals("conference")) {
                                try {
                                    mainController.filterForType(type);
                                } catch (Exception e) {
                                    sayError(e.getMessage());
                                }
                            }
                            else if(type.equals("work")) {
                                try {
                                    mainController.filterForType(type);
                                } catch (Exception e) {
                                    sayError(e.getMessage());
                                }
                            }
                            else {
                                sayError("Такого типа места не существует!");
                            }
                            break;
                        case "date":
                            try {
                                mainController.filterForDate();
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
    

    /**
     * Обрабатывает действия, связанные с управлением местами пользователя.
     * Позволяет пользователю добавить или удалить место из списка его мест.
     * @param myPlaces Список мест, принадлежащих пользователю.
     */
    public void myPlaceAction(Map<Integer, Place> myPlaces) {
        String command = new String();
        printList(myPlaces.values());
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
                        Integer id = Integer.parseInt(scanner.nextLine());
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
                            Integer i = Integer.parseInt(scanner.nextLine());
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
    
    /**
     * Обрабатывает действия бронирования места, на день указаный после команды searchDay главного меню.
     * Позволяет пользователю забронировать место, указав ID и желаемый временной интервал.
     * @param reservationByPlace Словарь где ключем выступает id места, а значением лист записей на это место.
     */
    public void reservation(Map<Integer, List<Reservation>> reservationByPlace, LocalDate date) {
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
                        Integer i = Integer.parseInt(scanner.nextLine());
                        System.out.println("Введите интервал желаемой записи в формате HH:mm HH:mm: ");
                        String inpuString = scanner.nextLine();
                        String[] strings = inpuString.split(" ");
                        LocalTime startTime = LocalTime.parse(strings[0], formatterTime);
                        LocalTime endTime = LocalTime.parse(strings[1], formatterTime);
                        if(endTime.isBefore(startTime)) {
                            throw new Exception("Неправильный временной порядок!");
                        }
                        mainController.reservating(reservationByPlace, i, startTime, endTime, date);
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

    /**
     * Выводит список мест в консоль.
     * @param list Список мест для вывода.
     */
    public void printList(Collection<Place> list) {
        StringBuilder sb = new StringBuilder();

        for(Place place : list) {
            sb.append(place.toString());
            sb.append("\n"); 
        }
        System.out.println(sb.toString());
    }

    /**
     * Выводит строку в консоль.
     * @param str Строка для вывода.
     */
    public void print(String str) {
        System.out.println(str);
    }

    /**
     * Отображает сообщение об ошибке.
     * Информирует пользователя о возникшей ошибке и предлагает повторить попытку.
     * @param errorMessage Сообщение об ошибке для отображения пользователю.
     */
    public void sayError(String errorMessage) {
        System.out.println("Произошла ошибка: ");
        System.out.println(errorMessage);
        System.out.println("Попробуйте снова.");
    }
}
