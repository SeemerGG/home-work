package com.example.in.controllers;

import java.security.NoSuchAlgorithmException;

import com.example.in.security.PasswordHashing;
import com.example.in.views.AutentificationView;
import com.example.model.User;
import com.example.out.dao.ConferenceRoomDAO;
import com.example.out.dao.ReservationDAO;
import com.example.out.dao.UserDAO;
import com.example.out.dao.WorkPlaceDAO;

public class AutentificationController {
    private AutentificationView view;
    private UserDAO userDAO;
    private MainController mainController;

    public AutentificationController(UserDAO userDAO) {
        this.view = new AutentificationView(this);
        this.userDAO = userDAO;
        this.view.run();
    }

    public void authorization(String login, String password) throws NoSuchAlgorithmException {
        User user = userDAO.getUser(login);
        try {
            if(user != null) {
                if(PasswordHashing.compareHashAndString(user.getPassword(), password)) {
                    mainController = new MainController(login, new WorkPlaceDAO(), new ConferenceRoomDAO(), new ReservationDAO());
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

    public void registration(String login, String password) throws NoSuchAlgorithmException {
        try {
            String hashPassword = PasswordHashing.getPasswordHash(password);
            userDAO.addUser(login, hashPassword);
            mainController = new MainController(login, new WorkPlaceDAO(), new ConferenceRoomDAO(), new ReservationDAO());
            mainController.authorized(login);
        }
        catch(Exception e)
        {
            view.sayError(e.getMessage());
        }
    }

}
