package com.nanal.backend.global.config;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Slf4j
@Aspect
@Component
public class AspectConfig {

    @Around("execution(* com..domain..*Service.*(..))")
    public Object logging(ProceedingJoinPoint joinPoint) throws Throwable {

        StopWatch stopWatch = new StopWatch();


        log.info("start - {}", joinPoint.getSignature().getName());

        stopWatch.start();
        Object result = joinPoint.proceed();
        stopWatch.stop();

        log.info("finished - {}", joinPoint.getSignature().getName());

        log.info("실행 시간 - {}", stopWatch.getTotalTimeMillis());

        return result;
    }

}
