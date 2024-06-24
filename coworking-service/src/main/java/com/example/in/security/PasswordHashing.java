package com.example.in.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Класс для хеширования паролей и проверки их соответствия.
 * Использует алгоритм SHA-256 для создания безопасного хеша пароля.
 */
public class PasswordHashing {

    /**
     * Генерирует хеш пароля с использованием алгоритма SHA-256.
     * @param password Пароль для хеширования.
     * @return Хеш-строка пароля в шестнадцатеричном формате.
     * @throws NoSuchAlgorithmException Если алгоритм хеширования не поддерживается.
     */
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

    /**
     * Сравнивает предоставленный хеш с хешем введенной строки.
     * @param originalHash Исходный хеш для сравнения.
     * @param inputString Строка, для которой будет сгенерирован хеш и сравнен с исходным.
     * @return true, если хеши совпадают, иначе false.
     * @throws NoSuchAlgorithmException Если алгоритм хеширования не поддерживается.
     */
    public static boolean compareHashAndString(String originalHash, String inputString) throws NoSuchAlgorithmException {
        String inputHash = getPasswordHash(inputString);
        return (originalHash.equals(inputHash)) ? true : false;
    } 
}
