package com.example.in.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpStatus;

import com.example.annotation.Loggable;
import com.example.annotation.LoggableHttp;
import com.example.dto.IntervalTimeDTO;
import com.example.dto.ReservationDTO;
import com.example.in.security.TokenCreator;
import com.example.in.service.ReservationService;
import com.example.model.Reservation;
import com.fasterxml.jackson.databind.ObjectMapper;

@Loggable
@LoggableHttp
@WebServlet("/reservation")
/**
 * Класс отвечающий за обработку запросов связанных с бронированием.
 */
public class ManipulationReservationServlet extends HttpServlet{

    private final ReservationService resService;
    private final ObjectMapper objectMapper;
    
    public ManipulationReservationServlet(ObjectMapper objectMapper, ReservationService resService) {

        this.resService = resService;
        this.objectMapper = objectMapper;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try {
            String token = req.getHeader("Authorization").replace("Bearer ", "");
            if(TokenCreator.verifyToken(token)) {  
                String login = TokenCreator.getUserLogin(token);
                String type = req.getParameter("filter");
                Collection<Reservation> reservs = new ArrayList<>();
                switch (type) {
                    case "day":
                        reservs = resService.filterForDate(login);
                        break;
                    case "owner":
                        reservs = resService.filterForOwner(login);
                        break;
                    case "type":
                        reservs = resService.filterForType(login);
                        break;
                }
                String json = objectMapper.writeValueAsString(reservs);
                resp.getWriter().write(json);
                resp.setStatus(HttpStatus.SC_OK);
            }
        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try {
            String token = req.getHeader("Authorization").replace("Bearer ", "");
            if(TokenCreator.verifyToken(token)) {  
                String login = TokenCreator.getUserLogin(token);
                int idReserv = Integer.parseInt(req.getParameter("id"));
                resService.deleteReservaton(idReserv, login);
                resp.setStatus(HttpStatus.SC_OK);
            }
        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String jwtToken = req.getHeader("Authorization").replace("Bearer ", "");
        try {
            if(TokenCreator.verifyToken(jwtToken)) {
                String login = TokenCreator.getUserLogin(jwtToken);
                StringBuilder jsonString = new StringBuilder();
                String line;
                BufferedReader reader = req.getReader();
                while((line = reader.readLine()) != null) {
                    jsonString.append(line);
                }
                ReservationDTO res = objectMapper.readValue(jsonString.toString(), ReservationDTO.class);
                if(resService.reservating(res.idPlace(), res.startTime(), res.endTime(), res.date(), login)) {
                    resp.setStatus(HttpStatus.SC_OK);
                }
                else {
                    resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Операция не выполнена.");
                }
            }
            else {
                resp.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Время жизни токена истекло.");
            }
        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try {
            String token = req.getHeader("Authorization").replace("Bearer ", "");
            if(TokenCreator.verifyToken(token)) {  
                String login = TokenCreator.getUserLogin(token);
                StringBuilder jsonString = new StringBuilder();
                String line;
                BufferedReader reader = req.getReader();
                while((line = reader.readLine()) != null) {
                    jsonString.append(line);
                }
                IntervalTimeDTO interval = objectMapper.readValue(jsonString.toString(), IntervalTimeDTO.class);
                int idReserv = Integer.parseInt(req.getParameter("id"));
                resService.updateTime(idReserv, interval.startTime(), interval.endTime(), login);
                resp.setStatus(HttpStatus.SC_OK);
            }
        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
        super.doPut(req, resp);
    }
}
