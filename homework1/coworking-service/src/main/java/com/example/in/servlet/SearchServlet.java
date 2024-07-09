package com.example.in.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpStatus;

import com.example.in.security.TokenCreator;
import com.example.in.service.SearchService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebServlet("/place/all")
public class SearchServlet extends HttpServlet{

    private final ObjectMapper objectMapper;
    private final SearchService searchService;

    /**
     * Конструктор класса.
     */
    public SearchServlet() {
        this.objectMapper = new ObjectMapper().findAndRegisterModules();
        this.searchService = new SearchService();
    }
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String jwtToken = req.getHeader("Authorization").replace("Bearer ", "");
        try {
            if(TokenCreator.verifyToken(jwtToken)) {
                String json = objectMapper.writeValueAsString(searchService.search());
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
