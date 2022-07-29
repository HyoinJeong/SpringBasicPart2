package org.prgms.kdt.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.prgms.kdt.JdkProxyTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {
    private static final Logger log = LoggerFactory.getLogger(LoggingAspect.class);


    @Around("org.prgms.kdt.aop.CommonPointcut.repositoryInsertMethodPointcut()")
    public Object log(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("Before method called. {}",joinPoint.getSignature().toString());
        var result=joinPoint.proceed();
        log.info("After method called with result => {}",result);
        return result;
    }
}