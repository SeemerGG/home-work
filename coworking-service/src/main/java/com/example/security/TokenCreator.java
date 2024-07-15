package com.example.security;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.infrastructure.Properties;

/**
 * Класс взаимодействия с токеном.
 */
public class TokenCreator {

    @Autowired
    private static Properties properties;

    private static final Algorithm algorithm = Algorithm.HMAC256(properties.getProperty("secretKey"));

    private static final long tokenLifetime = Integer.parseInt(properties.getProperty("tokenLifetime"));


    private static final String issuer = properties.getProperty("issuer");

    /**
     * Метод генерации JWT токена.
     * @param login Логин пользователя.
     * @return JWT токен. 
     */
    public static String generateToken(String login) {
        
        String jwtToken = JWT.create()
                            .withIssuer(issuer)
                            .withClaim("login", login)
                            .withExpiresAt(new Date(System.currentTimeMillis() + tokenLifetime))
                            .sign(algorithm);       

        return jwtToken;
    }
    
    /**
     * Метод верификации токена.
     * @param token Токен.
     * @return True если токен не просрочен, иначе False.
     */
    public static boolean verifyToken(String token) {

        try {
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(issuer)
                    .build();

            DecodedJWT decodedJWT = verifier.verify(token);

            if(decodedJWT.getExpiresAt().before(new Date(System.currentTimeMillis()))) {
                return false;
            }
        } catch (JWTVerificationException ex) {
            return false;
        }
        return true;
    }

    /**
     * Метод получения логина пользователя.
     * @param token Токен.
     * @return True если токен не просрочен, иначе False.
     */
    public static String getUserLogin(String token) {
        JWTVerifier verifier = JWT.require(algorithm)
                .withIssuer(issuer)
                .build();

        DecodedJWT decodedJWT = verifier.verify(token);

        return decodedJWT.getClaim("login").asString();
    }
}
