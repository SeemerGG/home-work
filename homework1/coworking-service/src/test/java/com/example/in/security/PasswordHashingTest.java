package com.example.in.security;

import java.security.NoSuchAlgorithmException;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class PasswordHashingTest {

    @Test
    void testCompareHashAndString_Success() throws NoSuchAlgorithmException {
        String password = "password";
        String passwordHash = PasswordHashing.getPasswordHash("password");
        assertTrue(PasswordHashing.compareHashAndString(passwordHash, password));
    }

    @Test
    void testCompareHashAndString_Unsuccess() throws NoSuchAlgorithmException {
        String password = "password2";
        String passwordHash = PasswordHashing.getPasswordHash("password");
        assertFalse(PasswordHashing.compareHashAndString(passwordHash, password));
    }

    
}
