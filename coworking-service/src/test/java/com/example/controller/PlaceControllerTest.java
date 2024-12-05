package com.example.controller;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.example.mapper.PlaceMapper;
import com.example.security.TokenCreator;
import com.example.service.PlaceService;

import java.util.Collections;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PlaceControllerTest {

    private MockMvc mockMvc;

    @Mock
    private PlaceService placeService;

    @Mock
    private PlaceMapper placeMapper;

    @Mock
    private TokenCreator tokenCreator;

    @InjectMocks
    private PlaceController placeController;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(placeController).build();
    }

    @Test
    @DisplayName("Проверка получения мест при корректных данных.")
    public void testGetPlaces_Success() throws Exception {

        String authHeader = "Bearer validToken";
        String userLogin = "user0001";
        when(tokenCreator.verifyToken(anyString())).thenReturn(true);
        when(tokenCreator.getUserLogin(anyString())).thenReturn(userLogin);
        when(placeService.myPublication(anyString())).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/places")
                        .header("Authorization", authHeader))
                        .andExpect(status().isOk());
    }
}
