package com.example.controller;

import java.util.ArrayList;
import java.util.Collection;

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

import com.example.annotation.Loggable;
import com.example.annotation.LoggableHttp;
import com.example.domain.dto.ReservationDTO;
import com.example.domain.model.Reservation;
import com.example.mapper.ReservationMapper;
import com.example.security.TokenCreator;
import com.example.service.ReservationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@Tag(name = "Контроллер управления бронированиями.", description = "Задает методы управления бронированиями.")
@LoggableHttp
@Loggable
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService reservService;
    private final ReservationMapper reservationMapper;
    private final TokenCreator tokenCreator;

    @Operation(summary = "Возвращает все бронирования зарегистрированные пользователем.", tags = "getReservations")
    @ApiResponses( value = {
        @ApiResponse(
            responseCode = "200",
            description = "Данные успешно отправлены.",
            content = {
                @Content(
                        mediaType = "application/json",
                        array = @ArraySchema(schema = @Schema(implementation = ReservationDTO.class)))
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

    @Operation(summary = "Удаляет бронирование зарегистрированное пользователем.", tags = "deleteReservation")
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

    @Operation(summary = "Создает новое бронирование.", tags = "addReservation")
    @ApiResponses( value = {
        @ApiResponse(
            responseCode = "200",
            description = "Данные успешно отправлены."),
        @ApiResponse(
            responseCode = "401",
            description = "Ошибка авторизации."
        ),
        @ApiResponse(
            responseCode = "501",
            description = "Возникла ошибка."
        )
    })
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

    @Operation(summary = "Изменяет время бронирования бронирования.", tags = "updateReservation")
    @ApiResponses( value = {
        @ApiResponse(
            responseCode = "200",
            description = "Данные успешно отправлены."),
        @ApiResponse(
            responseCode = "401",
            description = "Ошибка авторизации."
        ),
        @ApiResponse(
            responseCode = "501",
            description = "Возникла ошибка."
        )
    })
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
