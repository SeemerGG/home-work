package com.example.security;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import jakarta.annotation.PostConstruct;

/**
 * Класс взаимодействия с токеном.
 */
@Component
public class TokenCreator {

    @Value("${jwt.secretKey}")
    private String secretKey;

    @Value("${jwt.tokenLifetime}")
    private long tokenLifetime;

    @Value("${jwt.issuer}")
    private String issuer;

    private Algorithm algorithm;

    @PostConstruct
    public void init() {
        this.algorithm = Algorithm.HMAC256(secretKey);
    }

    /**
     * Метод генерации JWT токена.
     * @param login Логин пользователя.
     * @return JWT токен. 
     */
    public String generateToken(String login) {
        
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
    public boolean verifyToken(String token) {

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
    public String getUserLogin(String token) {
        JWTVerifier verifier = JWT.require(algorithm)
                .withIssuer(issuer)
                .build();

        DecodedJWT decodedJWT = verifier.verify(token);

        return decodedJWT.getClaim("login").asString();
    }
}
