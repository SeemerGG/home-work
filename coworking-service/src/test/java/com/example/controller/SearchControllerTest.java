package com.example.controller;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.example.security.TokenCreator;
import com.example.service.SearchService;

@ExtendWith(MockitoExtension.class)
public class SearchControllerTest {

    private MockMvc mockMvc;

    @Mock
    private SearchService searchService;

    @Mock
    private TokenCreator tokenCreator;

    @InjectMocks
    private SearchController searchController;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(searchController).build();
    }

    @Test
    public void testGetAllPlace_ValidToken() throws Exception {
        String token = "token";
        String authHeader = "Bearer " + token;
        when(tokenCreator.verifyToken(anyString())).thenReturn(true);
        when(searchService.search()).thenReturn(Collections.emptyList());
        
        mockMvc.perform(get("/search/place")
                .header("Authorization", authHeader)
                .contentType("application/json"))
                .andExpect(status().isOk());

        verify(searchService, times(1)).search();
    }
}
