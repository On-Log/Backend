package com.nanal.backend.global.config;

import com.nanal.backend.domain.analysis.entity.*;
import com.nanal.backend.domain.analysis.repository.*;
import com.nanal.backend.global.security.AuthenticationUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.security.web.firewall.RequestRejectedException;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
@Slf4j
@Aspect
@Component
public class AspectConfig {

    private final DiaryLogRepository diaryLogRepository;
    private final MypageLogRepository mypageLogRepository;
    private final RetrospectLogRepository retrospectLogRepository;
    private final AuthLogRepository authLogRepository;
    private final OnBoardingLogRepository onBoardingLogRepository;

    @Around("execution(* com..onboarding..*Service.*(..))")
    public Object onBoardingLogging(ProceedingJoinPoint joinPoint) throws Throwable {
        StopWatch stopWatch = new StopWatch();
        String email = AuthenticationUtil.getCurrentUserEmail();
        String methodName = joinPoint.getSignature().getName();

        stopWatch.start();
        Object result = joinPoint.proceed();
        stopWatch.stop();
        Long executionTime = stopWatch.getTotalTimeMillis();

        log.info("[{}] {} - FINISHED | EXECUTION TIME => {} ms", email, methodName, executionTime);

        OnBoardingLog onBoardingLog = OnBoardingLog.builder()
                .userEmail(email)
                .serviceName(methodName)
                .executionTime(executionTime)
                .build();

        onBoardingLogRepository.save(onBoardingLog);

        return result;
    }

    @Around("execution(* com..diary..*Service.*(..))")
    public Object diaryLogging(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        for (int i = 0; i < args.length; i++) {
            System.out.println(args[i]);
        }
        System.out.println("" + this);

        StopWatch stopWatch = new StopWatch();
        String email = AuthenticationUtil.getCurrentUserEmail();
        String methodName = joinPoint.getSignature().getName();

        stopWatch.start();
        Object result = joinPoint.proceed();
        stopWatch.stop();
        Long executionTime = stopWatch.getTotalTimeMillis();

        log.info("[{}] {} - FINISHED | EXECUTION TIME => {} ms", email, methodName, executionTime);

        DiaryLog diaryLog = DiaryLog.builder()
                .userEmail(email)
                .serviceName(methodName)
                .executionTime(executionTime)
                .build();

        diaryLogRepository.save(diaryLog);

        return result;
    }

    @Around("execution(* com..diary..*Service.*(..))")
    public Object test(ProceedingJoinPoint joinPoint) throws Throwable {

        System.out.println("여기는 테스트@@@@@@@@@@@@@@@@@@@@@@@ : " + this);
        Object result = joinPoint.proceed();
        System.out.println("여기는 테스트!!!!!!!!!!!!!!!!!!!!!!!!");
        return result;
    }

    @Around("execution(* com..mypage..*Service.*(..))")
    public Object myPageLogging(ProceedingJoinPoint joinPoint) throws Throwable {

        StopWatch stopWatch = new StopWatch();
        String email = AuthenticationUtil.getCurrentUserEmail();
        String methodName = joinPoint.getSignature().getName();

        stopWatch.start();
        Object result = joinPoint.proceed();
        stopWatch.stop();
        Long executionTime = stopWatch.getTotalTimeMillis();

        log.info("[{}] {} - FINISHED | EXECUTION TIME => {} ms", email, methodName, executionTime);

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

        stopWatch.start();
        Object result = joinPoint.proceed();
        stopWatch.stop();
        Long executionTime = stopWatch.getTotalTimeMillis();

        log.info("[{}] {} - FINISHED | EXECUTION TIME => {} ms", email, methodName, executionTime);

        RetrospectLog retrospectLog = RetrospectLog.builder()
                .userEmail(email)
                .serviceName(methodName)
                .executionTime(executionTime)
                .build();

        retrospectLogRepository.save(retrospectLog);

        return result;
    }

    @AfterReturning("execution(* com..AuthService.commonAuth(..))")
    public void authLogging(JoinPoint joinPoint) {

        String email = AuthenticationUtil.getCurrentUserEmail();
        String methodName = joinPoint.getSignature().getName();

        log.info("[{}] 로그인/회원가입 및 토큰 발급 완료", email);

        AuthLog authLog = AuthLog.builder()
                .userEmail(email)
                .serviceName(methodName)
                .build();

        authLogRepository.save(authLog);
    }

    @Around("execution(public void org.springframework.security.web.FilterChainProxy.doFilter(..))")
    public void handleRequestRejectedException (ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            joinPoint.proceed();
        } catch (RequestRejectedException exception) {
            log.info(exception.toString());
            HttpServletResponse response = (HttpServletResponse) joinPoint.getArgs()[1];
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}
