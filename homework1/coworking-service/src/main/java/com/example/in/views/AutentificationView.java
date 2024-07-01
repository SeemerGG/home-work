package com.example.in.views;

import java.util.Scanner;

import com.example.in.controllers.AutentificationController;

/**
 * Представление для аутентификации пользователя в системе Coworking-Service.
 * Обеспечивает интерфейс для входа в систему, регистрации нового пользователя и выхода из приложения.
 */
public class AutentificationView {

    private AutentificationController controller;
    private Scanner scanner = new Scanner(System.in);

    /**
     * Конструктор для создания представления аутентификации.
     * @param controller Контроллер аутентификации, управляющий процессами входа и регистрации.
     */
    public AutentificationView(AutentificationController controller) {
        this.controller = controller;
    }

    /**
     * Запускает интерфейс аутентификации.
     * Отображает приветственное сообщение и переключает действия в зависимости от ввода пользователя.
     */
    public void run() {
        System.out.println("***Вход в Coworking-Service***");
        switchAction();   
    }

    /**
     * Управляет действиями пользователя в меню аутентификации.
     * Позволяет пользователю выбрать между входом(in), регистрацией(up) и выходом из приложения(exit).
     */
    private void switchAction() {
        String str = new String();
        while (true) {
            System.out.println("Вход(введите in)/Регистрация(введите up)/Выход(введите exit)");   
            str = scanner.nextLine();
            if(str.equals("exit")){
                return;
            }
            switch (str) {
                case "in":
                    authorization();
                    break;
                case "up":
                    registration();
                    break;
                default:
                    System.out.println("Вы неверно ввели команду, попробуйте снова: ");
                    break;
            }
        }
    }

    /**
     * Обрабатывает процесс аутентификации пользователя.
     * Запрашивает у пользователя логин и пароль, передает их в контроллер для проверки.
     */
    public void authorization() {
        try {
            String login;
            String password;    
            System.out.println("Логин: ");
            login = scanner.nextLine();
            System.out.println("Пароль: ");
            password = scanner.nextLine();
            controller.authorization(login, password);
        } catch (Exception e) {
            sayError(e.getMessage());
        }
    }
    
    /**
     * Обрабатывает процесс регистрации нового пользователя.
     * Запрашивает у пользователя логин и пароль, передает их в контроллер для создания новой учетной записи.
     */
    public void registration() {
        try {
            String login;
            String password;
            System.out.println("Логин: ");
            login = scanner.nextLine();
            System.out.println("Пароль: ");
            password = scanner.nextLine();
            controller.registration(login, password);
        } catch (Exception e) {
            sayError(e.getMessage());
        }
    }

    /**
     * Отображает сообщение об ошибке.
     * Информирует пользователя о возникшей ошибке и предлагает повторить попытку.
     * @param errorMessage Сообщение об ошибке для отображения пользователю.
     */
    public void sayError(String errorMessage) {
        System.out.println("Произошла ошибка: ");
        System.out.println(errorMessage);
        System.out.println("Попробуйте снова: ");
    }
}
