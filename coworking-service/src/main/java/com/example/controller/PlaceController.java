package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.domain.dto.PlaceDTO;
import com.example.domain.model.Place;
import com.example.mapper.PlaceMapper;
import com.example.security.TokenCreator;
import com.example.service.PlaceService;

@RestController
@RequestMapping("/places")
public class PlaceController {

    private final PlaceService placeService;
    private final PlaceMapper placeMapper;
    private final TokenCreator tokenCreator;
    
    @Autowired
    public PlaceController(PlaceService placeService, PlaceMapper placeMapper, TokenCreator tokenCreator) {

        this.placeService = placeService;
        this.placeMapper = placeMapper;
        this.tokenCreator = tokenCreator;
    }

    @GetMapping()
    public ResponseEntity<?> getPlaces(@RequestHeader("Authorization") String authHeader) {
        
        String token = authHeader.replace("Bearer ", "");
        try {
            if(tokenCreator.verifyToken(token)) {
                return ResponseEntity.ok()
                                     .contentType(MediaType.APPLICATION_JSON)
                                     .body(placeService.myPublication(tokenCreator.getUserLogin(token)));
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
    public ResponseEntity<?> deletePlace(@RequestHeader("Authorization") String authHeader, @RequestParam("id") int id) {
        String token = authHeader.replace("Bearer ", "");
        try {
            if(tokenCreator.verifyToken(token)) {
                placeService.deleteMyPlace(id, tokenCreator.getUserLogin(token));
                return ResponseEntity.status(HttpStatus.OK).build();
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

    @PostMapping("/create")
    public ResponseEntity<?> updateTime(@RequestHeader("Authorization") String authHeader, 
    @RequestBody PlaceDTO placeDTO) {
        
        String token = authHeader.replace("Bearer ", "");
        try {
            if(tokenCreator.verifyToken(token)) {
                Place place = placeMapper.DtoToModel(placeDTO);
                placeService.createMyPlace(place);
                return ResponseEntity.status(HttpStatus.OK).build();
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
