package com.example.in.servlet;

import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpStatus;

import com.example.annotation.Loggable;
import com.example.annotation.LoggableHttp;
import com.example.dto.PlaceDTO;
import com.example.in.security.TokenCreator;
import com.example.in.service.PlaceService;
import com.example.model.PlaceType;
import com.fasterxml.jackson.databind.ObjectMapper;

@Loggable
@LoggableHttp
@WebServlet("/places")
public class ManipulationPlaceServlet extends HttpServlet{

    private final ObjectMapper objectMapper;
    private final PlaceService placeService;

    public ManipulationPlaceServlet() {
        objectMapper = new ObjectMapper();
        placeService = new PlaceService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String jwtToken = req.getHeader("Authorization").replace("Bearer ", "");
        try {
            if(TokenCreator.verifyToken(jwtToken)) {
                resp.setCharacterEncoding("UTF-8");
                resp.setContentType("application/json");
                String json = objectMapper.writeValueAsString(placeService.myPublication(TokenCreator.getUserLogin(jwtToken)));
                resp.setStatus(HttpStatus.SC_OK);
                resp.getWriter().write(json);
            }
            else {
                resp.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Время жизни токена истекло.");
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
                int idPlace = Integer.parseInt(req.getParameter("id"));
                placeService.deleteMyPlace(idPlace, login);
                resp.setStatus(HttpStatus.SC_OK);
            }
        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
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
                PlaceDTO placeDTO = objectMapper.readValue(jsonString.toString(), PlaceDTO.class);
                placeService.createMyPlace(placeDTO.seats(), PlaceType.valueOf(placeDTO.placeType()), login);
                resp.setStatus(HttpStatus.SC_OK);
            }
        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
