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

import com.example.annotation.Loggable;
import com.example.annotation.LoggableHttp;
import com.example.domain.dto.PlaceDTO;
import com.example.domain.model.Place;
import com.example.mapper.PlaceMapper;
import com.example.security.TokenCreator;
import com.example.service.PlaceService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Контроллер управления местами.", description = "Задает методы управления местами.")
@LoggableHttp
@Loggable
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

    @Operation(summary = "Возвращает все места зарегистрированные пользователем.", tags = "getPlaces")
    @ApiResponses( value = {
        @ApiResponse(
            responseCode = "200",
            description = "Данные успешно отправлены.",
            content = {
                @Content(
                        mediaType = "application/json",
                        array = @ArraySchema(schema = @Schema(implementation = PlaceDTO.class)))
        }),
        @ApiResponse(
            responseCode = "501",
            description = "Возникла ошибка."
        )
    })
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

    @Operation(summary = "Удаляет место зарегистрированное пользователем.", tags = "deltePlace")
    @ApiResponses( value = {
        @ApiResponse(
            responseCode = "200",
            description = "Данные успешно удалены."),
        @ApiResponse(
            responseCode = "401",
            description = "Ошибка авторизации."
        ),
        @ApiResponse(
            responseCode = "501",
            description = "Возникла ошибка."
        )
    })
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

    @Operation(summary = "Добавляет новое место.", tags = "putPlace")
    @ApiResponses( value = {
        @ApiResponse(
            responseCode = "200",
            description = "Данные успешно отправлены."
            ),
        @ApiResponse(
            responseCode = "401",
            description = "Ошибка авторизации."
        ),
        @ApiResponse(
            responseCode = "501",
            description = "Возникла ошибка."
        )
    })
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
