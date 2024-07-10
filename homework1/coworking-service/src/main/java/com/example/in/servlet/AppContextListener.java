package com.example.in.servlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.example.in.service.AutentificationService;
import com.example.in.service.PlaceService;
import com.example.in.service.ReservationService;
import com.example.in.service.SearchService;
import com.fasterxml.jackson.databind.ObjectMapper;


public class AppContextListener implements ServletContextListener{

    private ObjectMapper objectMapper;
    private AutentificationService authService;
    private PlaceService placeService;
    private ReservationService resService;
    private SearchService searchService;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        objectMapper = new ObjectMapper().findAndRegisterModules();
        authService = new AutentificationService();
        placeService = new PlaceService();
        resService = new ReservationService();
        searchService = new SearchService();

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
