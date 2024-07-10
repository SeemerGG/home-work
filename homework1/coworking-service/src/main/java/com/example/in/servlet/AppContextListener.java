package com.example.in.servlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.example.in.service.AutentificationService;
import com.example.in.service.PlaceService;
import com.example.in.service.ReservationService;
import com.example.in.service.SearchService;
import com.example.infrastructure.database.DBSingleton;
import com.example.out.dao.PlaceDAO;
import com.example.out.dao.ReservationDAO;
import com.example.out.dao.UserDAO;
import com.fasterxml.jackson.databind.ObjectMapper;


public class AppContextListener implements ServletContextListener{

    private ObjectMapper objectMapper;
    private AutentificationService authService;
    private PlaceService placeService;
    private ReservationService resService;
    private SearchService searchService;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        PlaceDAO placeDAO = new PlaceDAO(DBSingleton.getInstance());
        ReservationDAO reservationDAO = new ReservationDAO(DBSingleton.getInstance());
        UserDAO userDAO = new UserDAO(DBSingleton.getInstance());

        objectMapper = new ObjectMapper().findAndRegisterModules();
        authService = new AutentificationService(userDAO);
        placeService = new PlaceService(placeDAO);
        resService = new ReservationService(placeDAO, reservationDAO);
        searchService = new SearchService(placeDAO, reservationDAO);

        AutentificationServlet autentificationServlet = new AutentificationServlet(objectMapper, authService);
        ManipulationPlaceServlet placeServlet = new ManipulationPlaceServlet(objectMapper, placeService);
        ManipulationReservationServlet reservationServlet = new ManipulationReservationServlet(objectMapper, resService);
        RegistrationServlet registrationServlet = new RegistrationServlet(objectMapper, authService);
        SearchDayServlet searchDayServlet = new SearchDayServlet(objectMapper, searchService);
        SearchServlet searchServlet = new SearchServlet(objectMapper, searchService);

        ServletContext sc = sce.getServletContext();
        sc.addServlet("authServlet", autentificationServlet);
        sc.addServlet("placeServlet", placeServlet);
        sc.addServlet("reservationServlet", reservationServlet);
        sc.addServlet("registrationServlet", registrationServlet);
        sc.addServlet("searchDayServlet",searchDayServlet);
        sc.addServlet("searchServlet", searchServlet);
    }
}
