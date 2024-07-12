package com.example.in.servlet;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.dto.PlaceDTO;
import com.example.in.security.TokenCreator;
import com.example.in.service.PlaceService;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
public class ManipulationPlaceServletTest {

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private PlaceService placeService;

    @InjectMocks
    private ManipulationPlaceServlet servlet;

    @Mock
    private MockedStatic<TokenCreator> mockedStatic;

    @Test
    @DisplayName("Проверка метода получения ")
    public void testDoGet_ValidToken(@Mock HttpServletRequest request, @Mock HttpServletResponse response) throws Exception {

        String token = "validToken";
        String userLogin = "userLogin";
        String jsonResponse = "jsonResponse";
        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        mockedStatic.when(() -> TokenCreator.verifyToken(token)).thenReturn(true);
        mockedStatic.when(() -> TokenCreator.getUserLogin(token)).thenReturn(userLogin);
        when(placeService.myPublication(userLogin)).thenReturn(new ArrayList<>());
        when(objectMapper.writeValueAsString(any())).thenReturn(jsonResponse);

        servlet.doGet(request, response);

        verify(response).setStatus(HttpServletResponse.SC_OK);
    }

    @Test
    public void testDoPost_ValidToken(@Mock HttpServletRequest request, @Mock HttpServletResponse response) throws Exception {
        
        String token = "validToken";
        String userLogin = "userLogin";
        String jsonBody = "{\n \"placeType\": \"WORKPLACE\",\n\"seats\": 1}";
        PlaceDTO placeDTO = new PlaceDTO("WORKPLACE", 1);
        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        mockedStatic.when(() -> TokenCreator.verifyToken(token)).thenReturn(true);
        mockedStatic.when(() -> TokenCreator.getUserLogin(token)).thenReturn(userLogin);
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(jsonBody)));
        when(objectMapper.readValue(anyString(), eq(PlaceDTO.class))).thenReturn(placeDTO);
        doNothing().when(placeService).createMyPlace(anyInt(), any(), anyString());

        servlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_OK);
    }

    @Test
    public void testDoDelete_ValidToken(@Mock HttpServletRequest request, @Mock HttpServletResponse response) throws Exception {
        String token = "validToken";
        String userLogin = "userLogin";
        int idPlace = 1;
        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        mockedStatic.when(() -> TokenCreator.verifyToken(token)).thenReturn(true);
        mockedStatic.when(() -> TokenCreator.getUserLogin(token)).thenReturn(userLogin);
        when(request.getParameter("id")).thenReturn(String.valueOf(idPlace));
        doNothing().when(placeService).deleteMyPlace(idPlace, userLogin);

        servlet.doDelete(request, response);

        verify(placeService).deleteMyPlace(idPlace, userLogin);
        verify(response).setStatus(HttpServletResponse.SC_OK);
    }

}
