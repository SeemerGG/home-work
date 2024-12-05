package com.example.controller;

import java.time.LocalDate;
import java.util.ArrayList;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.annotation.Loggable;
import com.example.annotation.LoggableHttp;
import com.example.domain.dto.PlaceDTO;
import com.example.domain.dto.SearchDayRepresentationDTO;
import com.example.security.TokenCreator;
import com.example.service.SearchService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequiredArgsConstructor
@LoggableHttp
@Loggable
@RequestMapping("/search")
public class SearchController {

    private final SearchService searchsService;
    private final TokenCreator tokenCreator;

    @Operation(summary = "Возвращает все места зарегистрированные на платформе.", tags = "getAllPlaces")
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
            responseCode = "401",
            description = "Ошибка авторизации."
        ),
        @ApiResponse(
            responseCode = "501",
            description = "Возникла ошибка."
        )
    })
    @GetMapping("/place")
    public ResponseEntity<?> getAllPlace(@RequestHeader("Authorization") String authHeader) {
        
        String token = authHeader.replace("Bearer ", "");
        try {
            if(tokenCreator.verifyToken(token)) {
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
    
    @Operation(summary = "Возвращает места свободные на конкретный день.", tags = "searchPlaceForDay")
    @ApiResponses( value = {
        @ApiResponse(
            responseCode = "200",
            description = "Данные успешно отправлены.",
            content = {
                @Content(
                        mediaType = "application/json",
                        array = @ArraySchema(schema = @Schema(implementation = SearchDayRepresentationDTO.class)))
        }),
        @ApiResponse(
            responseCode = "401",
            description = "Ошибка авторизации."
        ),
        @ApiResponse(
            responseCode = "501",
            description = "Возникла ошибка."
        )
    })
    @GetMapping("/day")
    public ResponseEntity<?> getPlaceForDay(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date, 
    @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.replace("Bearer ", "");
        try {
            if(tokenCreator.verifyToken(token)) {
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
