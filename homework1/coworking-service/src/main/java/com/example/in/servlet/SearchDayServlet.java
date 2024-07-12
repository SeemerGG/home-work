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
import com.example.dto.DateDTO;
import com.example.in.security.TokenCreator;
import com.example.in.service.SearchService;
import com.fasterxml.jackson.databind.ObjectMapper;

@Loggable
@LoggableHttp
@WebServlet("/searchForDay")
public class SearchDayServlet extends HttpServlet {
    private final ObjectMapper objectMapper;
    private final SearchService searchService;

    public SearchDayServlet(ObjectMapper objectMapper, SearchService searchService) {
        this.objectMapper = objectMapper;
        this.searchService = searchService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String jwtToken = req.getHeader("Authorization").replace("Bearer ", "");
        try {
            if(TokenCreator.verifyToken(jwtToken)) {
                StringBuilder jsonString = new StringBuilder();
                String line;
                BufferedReader reader = req.getReader();
                while((line = reader.readLine()) != null) {
                    jsonString.append(line);
                }
                resp.setCharacterEncoding("UTF-8");
                DateDTO dateDTO = objectMapper.readValue(jsonString.toString(), DateDTO.class);
                String json = objectMapper.writeValueAsString(searchService.searchDay(dateDTO.date()));
                resp.setStatus(HttpStatus.SC_OK);
                resp.setContentType("application/json");
                resp.getWriter().write(json);
            }
            else {
                resp.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Время жизни токена истекло.");
            }
        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
    
    

}
