package com.example.in.controllers;

import java.security.NoSuchAlgorithmException;

import com.example.in.security.PasswordHashing;
import com.example.in.views.AutentificationView;
import com.example.model.User;
import com.example.out.dao.PlaceDAO;
import com.example.out.dao.ReservationDAO;
import com.example.out.dao.UserDAO;

/**
 * Класс AutentificationController управляет процессом аутентификации пользователя.
 */
public class AutentificationController {
    private AutentificationView view;
    private UserDAO userDAO;
    private MainController mainController;

    /**
     * Конструктор класса AutentificationController.
     * @param userDAO DAO для доступа к данным пользователей.
     */
    public AutentificationController(UserDAO userDAO) {
        this.view = new AutentificationView(this);
        this.userDAO = userDAO;
    }

    /**
     * Метод запуска основного цикла приложения.
     */
    public void appRun() {
        this.view.run();
    }

    /**
     * Метод для авторизации пользователя.
     * @param login Логин пользователя.
     * @param password Пароль пользователя.
     * @throws NoSuchAlgorithmException Если алгоритм хеширования не поддерживается.
     */
    public void authorization(String login, String password) throws NoSuchAlgorithmException {
        User user = userDAO.getUser(login);
        try {
            if(user != null) {
                if(PasswordHashing.compareHashAndString(user.getPassword(), password)) {
                    mainController = new MainController(login, new PlaceDAO(), new ReservationDAO());
                    mainController.authorized(login);
                }
                else {
                    view.sayError("Неверный пароль!");
                }
            }
            else {
                view.sayError("Пользователя с таким логином не существует!");
            }
        } catch (Exception e) {
            view.sayError(e.getMessage());
        }
    }

     /**
     * Метод для регистрации нового пользователя.
     * @param login Логин нового пользователя.
     * @param password Пароль нового пользователя.
     * @throws NoSuchAlgorithmException Если алгоритм хеширования не поддерживается.
     */
    public void registration(String login, String password) throws NoSuchAlgorithmException {
        try {
            String hashPassword = PasswordHashing.getPasswordHash(password);
            userDAO.addUser(login, hashPassword);
            mainController = new MainController(login, new PlaceDAO(), new ReservationDAO());
            mainController.authorized(login);
        }
        catch(Exception e)
        {
            view.sayError(e.getMessage());
        }
    }

}
