package com.example.in.servlet;

import static org.mockito.Mockito.*;

import java.io.BufferedReader;
import java.io.StringReader;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.in.service.AutentificationService;
import com.example.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;

public class AutentificationServletTest {

    private AutentificationServlet servlet;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private AutentificationService autentificationService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        servlet = new AutentificationServlet(objectMapper, autentificationService);
    }

    @Test
    public void testDoPostSuccess() throws Exception {
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
    public void testDoPostFailure() throws Exception {
        User user = new User("user0001", "user0001");
        String userJson = objectMapper.writeValueAsString(user);
        Exception exception = new Exception("Authentication failed");

        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(userJson)));
        when(autentificationService.authorization(any(User.class))).thenThrow(exception);

        servlet.doPost(request, response);

        verify(response).sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, exception.getMessage());
    }
}
