package com.example.controller;

import java.time.LocalDate;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.security.TokenCreator;
import com.example.service.SearchService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/search")
public class SearchController {

    private final SearchService searchsService;

    @Autowired
    public SearchController(SearchService service) {
        this.searchsService = service;
    }

    @GetMapping("/place")
    public ResponseEntity<?> getAllPlace(@RequestHeader("Authorization") String authHeader) {
        
        String token = authHeader.replace("Bearer ", "");
        try {
            if(TokenCreator.verifyToken(token)) {
                return ResponseEntity.ok()
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .body(new ArrayList<>(searchsService.search()));
            }
            else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                     .body("Время жизни токена истекло.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body(e.getMessage());
        }
    }
    
    @GetMapping("/day")
    public ResponseEntity<?> getPlaceForDay(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date, 
    @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.replace("Bearer ", "");
        try {
            if(TokenCreator.verifyToken(token)) {
                return ResponseEntity.ok()
                                     .contentType(MediaType.APPLICATION_JSON)
                                     .body(searchsService.searchDay(date));
            }
            else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                     .body("Время жизни токена истекло.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body(e.getMessage());
        }
    }
}
