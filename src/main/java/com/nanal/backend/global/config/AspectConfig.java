package com.nanal.backend.global.config;

import com.nanal.backend.domain.analysis.entity.AuthLog;
import com.nanal.backend.domain.analysis.entity.DiaryLog;
import com.nanal.backend.domain.analysis.entity.MypageLog;
import com.nanal.backend.domain.analysis.entity.RetrospectLog;
import com.nanal.backend.domain.analysis.repository.AuthLogRepository;
import com.nanal.backend.domain.analysis.repository.DiaryLogRepository;
import com.nanal.backend.domain.analysis.repository.MypageLogRepository;
import com.nanal.backend.domain.analysis.repository.RetrospectLogRepository;
import com.nanal.backend.global.security.AuthenticationUtil;
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

        log.info("[{}]{} - FINISHED | EXECUTION TIME => {} ms", email, methodName, executionTime);

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

        log.info("[{}]{} - FINISHED | EXECUTION TIME => {} ms", email, methodName, executionTime);

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

        log.info("[{}]{} - FINISHED | EXECUTION TIME => {} ms", email, methodName, executionTime);

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
