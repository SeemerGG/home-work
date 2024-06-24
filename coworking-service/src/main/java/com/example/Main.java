package com.example;

import com.example.in.controllers.AutentificationController;
import com.example.out.dao.UserDAO;

/**
 * Главный класс приложения, отвечающий за запуск программы.
 */
public class Main {

    /**
     * Основной метод приложения.
     * Создает экземпляр AutentificationController с новым UserDAO, запускает основной цикл приложения.
     * @param args аргументы командной строки (не используются)
     */
    public static void main(String[] args) {
       AutentificationController controller = new AutentificationController(new UserDAO());
       controller.appRun();
    }
}