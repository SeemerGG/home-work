package com.example.in.views;

import java.util.Scanner;

import com.example.in.controllers.AutentificationController;

public class AutentificationView {

    private AutentificationController controller;
    private Scanner scanner = new Scanner(System.in);

    public AutentificationView(AutentificationController controller) {
        this.controller = controller;
    }

    public void run() {
        System.out.println("***Вход в Coworking-Service***");
        switchAction();   
    }

    private void switchAction() {
        String str = new String();
        while (true) {
            System.out.println("Вход(введите in)/Регистрация(введите up)/Выход(введите exit)");   
            str = scanner.nextLine();
            if(str.equals("exit")){
                System.exit(0);
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

    public void sayError(String errorMessage) {
        System.out.println("Произошла ошибка: ");
        System.out.println(errorMessage);
        System.out.println("Попробуйте снова: ");
    }
}
