package com.nanal.backend.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class TestController {
    private final RedisTemplate<String, String> redisTemplate;

    @GetMapping("/auth")
    public String auth() {
        log.info("hi auth user");
        return "auth success";
    }

    @GetMapping("/deploy")
    public String deploy() {
        log.info("hi deploy");
        return "deploy success";
    }

    @GetMapping("/redis")
    public String store() {

        // given
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        String key = "why";

        valueOperations.set(key, "hello");

        return "ok";
    }

    @GetMapping("/test/log")
    public void logTest() {
        log.info("info");
        log.warn("warn");
        log.error("error");
    }
}