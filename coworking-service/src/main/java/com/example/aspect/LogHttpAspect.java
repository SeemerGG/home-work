package com.example.aspect;

import java.time.LocalDateTime;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.example.out.dao.UsersActionLogDAO;
import com.example.security.TokenCreator;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Aspect
@Component
@RequiredArgsConstructor
public class LogHttpAspect {

    private final UsersActionLogDAO dao;
    private final TokenCreator tokenCreator; 
  
    @Pointcut("within(@com.example.annotation.LoggableHttp *) && execution(* *(..))")
    public void annotatedByLoggableHttp() {}

    @Before("annotatedByLoggableHttp()")
    public void logHttpRequest(JoinPoint joinPoint) throws Exception {
        HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        System.out.println(req + "FFFFFFFFFFFF");
        String token = req.getHeader("Authorization").replace("Bearer ", "");
        String login = tokenCreator.getUserLogin(token);
        String url = req.getRequestURI();
        dao.add(login, LocalDateTime.now(), url);
    }
}
