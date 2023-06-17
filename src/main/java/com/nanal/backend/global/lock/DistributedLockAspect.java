package com.nanal.backend.global.lock;

import com.nanal.backend.global.exception.customexception.InternalServerErrorException;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Aspect
@Component
@Order(1)
@RequiredArgsConstructor
public class DistributedLockAspect {
    private final RedissonClient redissonClient;
    private final RedisTemplate<String, String> redisTemplate;

    @Around("@annotation(com.nanal.backend.global.lock.DistributedLock)")
    public Object handleLettuceDistributedLock(ProceedingJoinPoint joinPoint) throws Throwable {
        String lockName = getLockName(joinPoint);
        System.out.println(lockName);

        // try 문 안으로 들어가면 예외 발생시키는 스레드가 임의로 락 해제 시킴
        if(!lock(lockName)) throw new InternalServerErrorException("lettuce 락 획득 실패");
        try {
            return joinPoint.proceed();
        } catch (InterruptedException e) {
            throw e;
        } finally {
            unlock(lockName);
        }
    }

    //    @Around("@annotation(com.nanal.backend.global.lock.DistributedLock)")
    public Object handleRedissonDistributedLock(ProceedingJoinPoint joinPoint) throws Throwable {
        String lockName = getLockName(joinPoint);
        RLock lock = redissonClient.getLock(lockName);

        try {
            boolean available = lock.tryLock(5, 1, TimeUnit.SECONDS);

            if(!available) throw DistributedLockException.EXCEPTION;

            return joinPoint.proceed();
        } catch (InterruptedException e) {
            throw e;
        } finally {
            lock.unlock();
        }
    }

    private String getLockName(ProceedingJoinPoint joinPoint) {
        Annotation[][] annotations = ((MethodSignature) joinPoint.getSignature()).getMethod().getParameterAnnotations();
        Object[] args = joinPoint.getArgs();
        for (int i = 0; i < args.length; i++) {
            for (Annotation annotation : annotations[i]) {
                if (annotation instanceof LockName) {
                    return args[i].toString();
                }
            }
        }
        throw new InternalServerErrorException("@LockName이 설정되어있지 않습니다.");
    }

    public Boolean lock(String key) {
        return redisTemplate
                .opsForValue()
                .setIfAbsent(key, "lock", Duration.ofMillis(3_000));
    }

    public Boolean unlock(String key) {
        return redisTemplate.delete(key);
    }

}