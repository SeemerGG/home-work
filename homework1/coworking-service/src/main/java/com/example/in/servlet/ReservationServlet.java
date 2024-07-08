package com.example.in.servlet;

import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpStatus;

import com.example.dto.ReservationDTO;
import com.example.in.security.TokenCreator;
import com.example.in.service.ReservationService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebServlet("/reservation")
public class ReservationServlet extends HttpServlet{

    private final ObjectMapper objectMapper;
    private final ReservationService reservationService;

    /**
     * Конструктор класса.
     */
    public ReservationServlet() {
        this.objectMapper = new ObjectMapper().findAndRegisterModules();
        this.reservationService = new ReservationService();
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
                if(reservationService.reservating(res.idPlace(), res.startTime(), res.endTime(), res.date(), login)) {
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

}
