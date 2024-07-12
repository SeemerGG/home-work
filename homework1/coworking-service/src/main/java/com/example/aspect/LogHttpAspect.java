package com.example.aspect;

import java.time.LocalDateTime;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import com.example.in.security.TokenCreator;
import com.example.infrastructure.database.DBSingleton;
import com.example.out.dao.UsersActionLogDAO;




@Aspect
public class LogHttpAspect {

    private static final UsersActionLogDAO dao = new UsersActionLogDAO(DBSingleton.getInstance());

    @Pointcut("within(@com.example.annotation.LoggableHttp *) && execution(* *(..))")
    private void annotatedByLoggableHttp() {}

    @AfterReturning("annotatedByLoggableHttp() && args(req, resp)")
    public void logHttpRequest(JoinPoint joinPoint, HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String jwtToken = req.getHeader("Authorization");
        if(jwtToken != null) { 
            jwtToken = jwtToken.replace("Bearer ", "");
            String login = TokenCreator.getUserLogin(jwtToken);
            String uri = req.getRequestURI();
            dao.add(login, LocalDateTime.now(), uri);
        }
    }
}
