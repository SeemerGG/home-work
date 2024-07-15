package com.example.aspect;

import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggableAspect implements BeanPostProcessor{

    
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("Calling method " + bean.getSignature());
        long start = System.currentTimeMillis();
        Object result = proceedingJoinPoint.proceed();
        long end = System.currentTimeMillis() - start;
        System.out.println("Execution of method " + proceedingJoinPoint.getSignature() + "finiched. Execution time is " + end + " ms.");
        return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        // TODO Auto-generated method stub
        return BeanPostProcessor.super.postProcessBeforeInitialization(bean, beanName);
    }

    @Pointcut("within(@com.example.annotation.Loggable *) && execution(* * (..))")
    public void annotatedByLoggable() {}

    @Around("annotatedByLoggable()")
    public Object logging(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        System.out.println("Calling method " + proceedingJoinPoint.getSignature());
        long start = System.currentTimeMillis();
        Object result = proceedingJoinPoint.proceed();
        long end = System.currentTimeMillis() - start;
        System.out.println("Execution of method " + proceedingJoinPoint.getSignature() + "finiched. Execution time is " + end + " ms.");
        return result;
    }
}
