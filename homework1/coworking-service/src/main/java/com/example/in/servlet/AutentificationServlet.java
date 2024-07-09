package com.example.in.servlet;

import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.annotation.Loggable;
import com.example.annotation.LoggableHttp;
import com.example.in.service.AutentificationService;
import com.example.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;

@Loggable
@LoggableHttp
@WebServlet("/auth")
public class AutentificationServlet extends HttpServlet {

    private final ObjectMapper objectMapper;
    private final AutentificationService autentificationService;

    public AutentificationServlet() {
        this.objectMapper = new ObjectMapper();
        this.autentificationService = new AutentificationService();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        StringBuilder jsonString = new StringBuilder();
        String line;
        BufferedReader reader = req.getReader();
        while((line = reader.readLine()) != null) {
            jsonString.append(line);
        }
        User user = objectMapper.readValue(jsonString.toString(), User.class);
        try {
            String token = autentificationService.authorization(user);
            resp.setHeader("Authorization", "Bearer " + token);
            resp.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
