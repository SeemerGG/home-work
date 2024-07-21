package com.example.in.security;

import java.security.NoSuchAlgorithmException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.example.security.PasswordHashing;

import static org.junit.jupiter.api.Assertions.*;


public class PasswordHashingTest {

    @Test
    @DisplayName("Сравнение одинаковых паролей.")
    void testCompareHashAndString_Success() throws NoSuchAlgorithmException {

        String password = "password";
        String passwordHash = PasswordHashing.getPasswordHash("password");
        assertTrue(PasswordHashing.compareHashAndString(passwordHash, password));
    }

    @Test
    @DisplayName("Сравнение разных паролей.")
    void testCompareHashAndString_Unsuccess() throws NoSuchAlgorithmException {
        
        String password = "password2";
        String passwordHash = PasswordHashing.getPasswordHash("password");
        assertFalse(PasswordHashing.compareHashAndString(passwordHash, password));
    }

    
}
