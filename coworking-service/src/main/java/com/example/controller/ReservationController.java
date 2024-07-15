package com.example.controller;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.domain.dto.ReservationDTO;
import com.example.domain.model.Reservation;
import com.example.mapper.ReservationMapper;
import com.example.security.TokenCreator;
import com.example.service.ReservationService;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService reservService;
    private final ReservationMapper reservationMapper;
    private final TokenCreator tokenCreator;
    
    @Autowired
    public ReservationController(ReservationService reservationService, ReservationMapper reservationMapper, TokenCreator tokenCreator) {

        this.reservService = reservationService;
        this.reservationMapper = reservationMapper;
        this.tokenCreator = tokenCreator;
    }

    @GetMapping()
    public ResponseEntity<?> getReservations(@RequestHeader("Authorization") String authHeader, 
    @RequestParam("type") String type) {

        String token = authHeader.replace("Bearer ", "");
        try {
            if(tokenCreator.verifyToken(token)) {
                Collection<Reservation> reservs = null;
                String login = tokenCreator.getUserLogin(token);
                switch (type) {
                    case "day":
                        reservs = reservService.filterForDate(login);
                        break;
                    case "owner":
                        reservs = reservService.filterForOwner(login);
                        break;
                    case "type":
                        reservs = reservService.filterForType(login);
                        break;
                }
                return ResponseEntity.ok()
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .body(new ArrayList<>(reservs));
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

    @DeleteMapping()
    public ResponseEntity<?> deleteReservation(@RequestHeader("Authorization") String authHeader, 
    @RequestParam("id") int id) {
        
        String token = authHeader.replace("Bearer ", "");
        try {
            if(tokenCreator.verifyToken(token)) {
                reservService.deleteReservaton(id, tokenCreator.getUserLogin(token));
                return ResponseEntity.ok().build();
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

    @PostMapping()
    public ResponseEntity<?> createReservation(@RequestHeader("Authorization") String authHeader, 
    @RequestBody ReservationDTO reservDto) {
        
        String token = authHeader.replace("Bearer ", "");
        try {
            if(tokenCreator.verifyToken(token)) {
                Reservation reservation = reservationMapper.dtoToModel(reservDto);
                reservService.reservating(reservation);
                return ResponseEntity.ok().build();
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

    @PutMapping("update/time")
    public ResponseEntity<?> updateTime(@RequestHeader("Authorization") String authHeader, 
    @RequestBody ReservationDTO reservDto) {
        
        String token = authHeader.replace("Bearer ", "");
        try {
            if(tokenCreator.verifyToken(token)) {
                Reservation reservation = reservationMapper.dtoToModel(reservDto);
                reservService.updateTime(reservation);
                return ResponseEntity.ok().build();
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
