package com.example;

import com.example.in.controllers.AutentificationController;
import com.example.out.dao.UserDAO;

public class Main {
    public static void main(String[] args) {
        AutentificationController autentificationController = new AutentificationController(new UserDAO());
    }
}