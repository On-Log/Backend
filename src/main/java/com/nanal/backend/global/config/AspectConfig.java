package com.nanal.backend.global.config;

import com.nanal.backend.entity.log.AuthLog;
import com.nanal.backend.entity.log.DiaryLog;
import com.nanal.backend.entity.log.MypageLog;
import com.nanal.backend.entity.log.RetrospectLog;
import com.nanal.backend.entity.log.repository.AuthLogRepository;
import com.nanal.backend.entity.log.repository.DiaryLogRepository;
import com.nanal.backend.entity.log.repository.MypageLogRepository;
import com.nanal.backend.entity.log.repository.RetrospectLogRepository;
import com.nanal.backend.global.auth.AuthenticationUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@RequiredArgsConstructor
@Slf4j
@Aspect
@Component
public class AspectConfig {

    private final DiaryLogRepository diaryLogRepository;
    private final MypageLogRepository mypageLogRepository;
    private final RetrospectLogRepository retrospectLogRepository;
    private final AuthLogRepository authLogRepository;

    @Around("execution(* com..diary..*Service.*(..))")
    public Object diaryLogging(ProceedingJoinPoint joinPoint) throws Throwable {

        StopWatch stopWatch = new StopWatch();
        String email = AuthenticationUtil.getCurrentUserEmail();
        String methodName = joinPoint.getSignature().getName();

        log.info("[{}]{} - START", email, methodName);

        stopWatch.start();
        Object result = joinPoint.proceed();
        stopWatch.stop();
        Long executionTime = stopWatch.getTotalTimeMillis();

        log.info("[{}]{} - FINISHED", email, methodName);

        log.info("[{}]{} - EXECUTION TIME => {}", email, methodName, executionTime);

        DiaryLog diaryLog = DiaryLog.builder()
                .userEmail(email)
                .serviceName(methodName)
                .executionTime(executionTime)
                .build();

        diaryLogRepository.save(diaryLog);

        return result;
    }

    @Around("execution(* com..mypage..*Service.*(..))")
    public Object myPageLogging(ProceedingJoinPoint joinPoint) throws Throwable {

        StopWatch stopWatch = new StopWatch();
        String email = AuthenticationUtil.getCurrentUserEmail();
        String methodName = joinPoint.getSignature().getName();

        log.info("[{}]{} - START", email, methodName);

        stopWatch.start();
        Object result = joinPoint.proceed();
        stopWatch.stop();
        Long executionTime = stopWatch.getTotalTimeMillis();

        log.info("[{}]{} - FINISHED", email, methodName);

        log.info("[{}]{} - EXECUTION TIME => {}", email, methodName, executionTime);

        MypageLog mypageLog = MypageLog.builder()
                .userEmail(email)
                .serviceName(methodName)
                .executionTime(executionTime)
                .build();

        mypageLogRepository.save(mypageLog);

        return result;
    }

    @Around("execution(* com..retrospect..*Service.*(..))")
    public Object retrospectLogging(ProceedingJoinPoint joinPoint) throws Throwable {

        StopWatch stopWatch = new StopWatch();
        String email = AuthenticationUtil.getCurrentUserEmail();
        String methodName = joinPoint.getSignature().getName();

        log.info("[{}]{} - START", email, methodName);

        stopWatch.start();
        Object result = joinPoint.proceed();
        stopWatch.stop();
        Long executionTime = stopWatch.getTotalTimeMillis();

        log.info("[{}]{} - FINISHED", email, methodName);

        log.info("[{}]{} - EXECUTION TIME => {}", email, methodName, executionTime);

        RetrospectLog retrospectLog = RetrospectLog.builder()
                .userEmail(email)
                .serviceName(methodName)
                .executionTime(executionTime)
                .build();

        retrospectLogRepository.save(retrospectLog);

        return result;
    }

    @Around("execution(* com..AuthController.signUp(..))")
    public Object authLogging(ProceedingJoinPoint joinPoint) throws Throwable {

        Object result = joinPoint.proceed();

        String email = AuthenticationUtil.getCurrentUserEmail();
        String methodName = joinPoint.getSignature().getName();

        log.info("[{}] Token Issue", email);

        AuthLog authLog = AuthLog.builder()
                .userEmail(email)
                .serviceName(methodName)
                .build();

        authLogRepository.save(authLog);

        return result;
    }
}
