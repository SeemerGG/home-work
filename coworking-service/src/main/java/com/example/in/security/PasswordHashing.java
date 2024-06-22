package com.example.in.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordHashing {

    public static String getPasswordHash(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(password.getBytes());
        byte[] hashedPassword = md.digest();

        // Конвертация байтового массива в шестнадцатеричный формат
        StringBuilder sb = new StringBuilder();
        for (byte b : hashedPassword) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    public static boolean compareHashAndString(String originalHash, String inputString) throws NoSuchAlgorithmException {
        String inputHash = getPasswordHash(inputString);
        return (originalHash.equals(inputHash)) ? true : false;
    } 
}
