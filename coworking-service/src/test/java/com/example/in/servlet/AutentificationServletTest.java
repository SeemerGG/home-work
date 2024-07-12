package com.example.in.servlet;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.io.BufferedReader;
import java.io.StringReader;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.in.service.AutentificationService;
import com.example.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
public class AutentificationServletTest {

    @Mock
    private AutentificationService autentificationService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private AutentificationServlet servlet;

    

    @BeforeEach
    public void setUp() {
        servlet = new AutentificationServlet(objectMapper, autentificationService);
    }

    @Test
    @DisplayName("Проверка успешной авторизации.")
    public void testDoPostSuccess(@Mock HttpServletRequest request, @Mock HttpServletResponse response) throws Exception {

        User user = new User("user0001", "user0001");
        String userJson = objectMapper.writeValueAsString(user);
        String token = "someToken";

        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(userJson)));
        when(autentificationService.authorization(any(User.class))).thenReturn(token);

        servlet.doPost(request, response);
        
        verify(response).setHeader("Authorization", "Bearer " + token);
        verify(response).setStatus(HttpServletResponse.SC_OK);
    }

    @Test
    @DisplayName("Проверка неудачной попытки авторизации.")
    public void testDoPostFailure(@Mock HttpServletRequest request, @Mock HttpServletResponse response) throws Exception {
        
        User user = new User("user0001", "user0001");
        String userJson = objectMapper.writeValueAsString(user);
        Exception exception = new Exception("Authentication failed");

        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(userJson)));
        when(autentificationService.authorization(any(User.class))).thenThrow(exception);

        servlet.doPost(request, response);

        verify(response).sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, exception.getMessage());
    }
}
