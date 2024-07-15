package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.domain.dto.UserDTO;
import com.example.domain.model.User;
import com.example.mapper.UserMapper;
import com.example.service.AuthentificationService;


@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final UserMapper userMapper;
    private final AuthentificationService authService;

    @Autowired
    public AuthenticationController(UserMapper mapper, AuthentificationService service) {
        this.userMapper = mapper;
        this.authService = service;
    }

    @GetMapping()
    public ResponseEntity<String> authentification(@RequestBody UserDTO userDTO) {
        
        try {
            User user = userMapper.DtoToModel(userDTO);
            String token = authService.authorization(user);
            return ResponseEntity.ok().header("Authorization", "Bearer " + token).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping()
    public ResponseEntity<String> getMethodName(@RequestBody UserDTO userDTO) {

        try {
            User user = userMapper.DtoToModel(userDTO);
            String token = authService.registration(user);
            return ResponseEntity.ok().header("Authorization", "Bearer " + token).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    
}
