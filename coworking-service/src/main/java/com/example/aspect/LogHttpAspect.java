package com.example.aspect;

// import java.time.LocalDateTime;

// import jakarta.servlet.http.HttpServletRequest;
// import jakarta.servlet.http.HttpServletResponse;

// import org.aspectj.lang.JoinPoint;
// import org.aspectj.lang.annotation.AfterReturning;
// import org.aspectj.lang.annotation.Aspect;
// import org.aspectj.lang.annotation.Pointcut;
// import org.aspectj.lang.reflect.MethodSignature;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.ResponseEntity;
// import org.springframework.stereotype.Component;

// import com.example.out.dao.UsersActionLogDAO;
// import com.example.security.TokenCreator;




// @Aspect
// @Component
public class LogHttpAspect {

    // private final UsersActionLogDAO dao;
    // private final TokenCreator tokenCreator; 

    // @Autowired
    // public LogHttpAspect(UsersActionLogDAO dao, TokenCreator tokenCreator) {

    //     this.dao = dao;
    //     this.tokenCreator = tokenCreator;
    // }

    // @Pointcut("within(@com.example.annotation.LoggableHttp *) && execution(* *(..))")
    // private void annotatedByLoggableHttp() {}

    // @AfterReturning(pointcut = "annotatedByLoggableHttp()")
    // public void logHttpRequest(JoinPoint joinPoint) throws Exception {
    //     MethodSignature signature = (MethodSignature) joinPoint.getSignature();
    //     Object[] args = joinPoint.getArgs();
    //     for(Object arg : args) {
    //         if(arg instanceof )
    //     }
    //     String jwtToken = req.getHeader("Authorization");
    //     if(jwtToken != null) { 
    //         jwtToken = jwtToken.replace("Bearer ", "");
    //         String login = tokenCreator.getUserLogin(jwtToken);
    //         String uri = req.getRequestURI();
    //         dao.add(login, LocalDateTime.now(), uri);
    //     }
    // }
}
