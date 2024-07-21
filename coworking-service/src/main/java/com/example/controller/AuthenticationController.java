package com.example.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.annotation.Loggable;
import com.example.domain.dto.UserDTO;
import com.example.domain.model.User;
import com.example.mapper.UserMapper;
import com.example.service.AuthentificationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;


@RestController
@RequiredArgsConstructor
@Tag(name = "Контроллер авторизации", description = "Задает методы авторизации и регистрации.")
@Loggable
@RequestMapping("/auth")
public class AuthenticationController {

    private final UserMapper userMapper;
    private final AuthentificationService authService;

    @Operation(summary = "Производит процесс авторизации.", tags = "authorization")
    @ApiResponses( value = {
        @ApiResponse(
            responseCode = "200",
            description = "Авторизация выполнена успешно. В ответ добавлен заголовок \"Authorization\" с токеном пользователя."
        ),
        @ApiResponse(
            responseCode = "401",
            description = "При авторизации возникла ошибка."
        )
    })
    @GetMapping()
    public ResponseEntity<String> authentification(@RequestBody UserDTO userDTO) {
        
        try {
            User user = userMapper.DtoToModel(userDTO);
            String token = authService.authorization(user);
            return ResponseEntity.ok().header("Authorization", "Bearer " + token).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @Operation(summary = "Производит процесс регистрации.", tags = "registration")
    @ApiResponses( value = {
        @ApiResponse(
            responseCode = "200",
            description = "Регистрация выполнена успешно. В ответ добавлен заголовок \\\"Authorization\\\" с токеном пользователя."
        ),
        @ApiResponse(
            responseCode = "401",
            description = "При регистрации возникла ошибка."
        )
    })
    @PostMapping()
    public ResponseEntity<String> registration(@RequestBody UserDTO userDTO) {

        try {
            User user = userMapper.DtoToModel(userDTO);
            String token = authService.registration(user);
            return ResponseEntity.ok().header("Authorization", "Bearer " + token).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    
}
