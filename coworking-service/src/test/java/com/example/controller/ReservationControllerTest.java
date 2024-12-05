package com.example.controller;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
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

import com.example.mapper.ReservationMapper;
import com.example.security.TokenCreator;
import com.example.service.ReservationService;

@ExtendWith(MockitoExtension.class)
public class ReservationControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ReservationService resService;

    @Mock
    private ReservationMapper resMapper;

    @Mock
    private TokenCreator tokenCreator;

    @InjectMocks
    private ReservationController resController;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(resController).build();
    }

    @Test
    @DisplayName("Проверка корректного удаления.")
    public void deleteReservation_WithValidToken_ReturnsOk() throws Exception {

        String header = "Bearer validToken";
        String login = "user0001";
        int reservationId = 1;

        when(tokenCreator.verifyToken(anyString())).thenReturn(true);
        when(tokenCreator.getUserLogin(anyString())).thenReturn(login);
        doNothing().when(resService).deleteReservaton(anyInt(), anyString());

        mockMvc.perform(delete("/reservations?id=" + reservationId)
                .header("Authorization", header))
                .andExpect(status().isOk());
        
        verify(resService, times(1)).deleteReservaton(eq(reservationId), eq(login));
    }
}
